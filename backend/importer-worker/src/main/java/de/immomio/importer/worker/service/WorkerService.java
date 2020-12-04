package de.immomio.importer.worker.service;

import de.immomio.cloud.service.s3.AbstractImageService;
import de.immomio.common.amazon.s3.S3FileManagerException;
import de.immomio.data.base.type.common.ImportStatus;
import de.immomio.data.base.type.property.PropertyStatus;
import de.immomio.data.base.type.property.PropertyTask;
import de.immomio.data.base.type.property.PropertyWriteProtection;
import de.immomio.data.landlord.bean.prioset.PriosetData;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.importlog.ImportLog;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.importer.ImportMailData;
import de.immomio.data.landlord.entity.property.importer.ImportObject;
import de.immomio.data.landlord.entity.property.openimmo.OpenImmoAktionTyp;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.ImportAttributes;
import de.immomio.data.shared.bean.common.PropertyDescriptorBean;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.entity.common.City;
import de.immomio.data.shared.entity.common.District;
import de.immomio.importer.worker.service.reporting.ImporterWorkerIndexingSenderService;
import de.immomio.messaging.converter.MessagingDefaultJackson2Serializer;
import de.immomio.model.repository.core.landlord.customer.BaseLandlordCustomerRepository;
import de.immomio.model.repository.core.landlord.customer.importlog.BaseImportLogRepository;
import de.immomio.model.repository.core.landlord.customer.prioset.BasePriosetRepository;
import de.immomio.model.repository.core.landlord.customer.property.BasePropertyRepository;
import de.immomio.model.repository.service.landlord.customer.user.LandlordUserRepository;
import de.immomio.utils.ReflectionUtils;
import de.immomio.utils.compare.CompareBean;
import de.immomio.utils.compare.DiffUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */
@Service
@Slf4j
public class WorkerService {

    private static final String INVALID_ZIPCODE = "INVALID_ZIPCODE";

    private static final List<PropertyDescriptorBean> protectedProperties;

    private static final List<PropertyDescriptorBean> nullProtectedProperties;

    static {
        protectedProperties = new ArrayList<>();
        nullProtectedProperties = new ArrayList<>();

        addProtectedProperty("id");
        addProtectedProperty("created");
        addProtectedProperty("customer");
        addProtectedProperty("user");
        addProtectedProperty("portals");
        addProtectedProperty("validUntil");
        // addProtectedProperty("booked", Property.class);
        addProtectedProperty("runtimeInDays");
        // addProtectedProperty("active", Property.class);
        // addProtectedProperty("state", Property.class);
        // addProtectedProperty("task", Property.class);
        addProtectedProperty("prioset");
        // addProtectedProperty("flatApplications", Property.class);
        // addProtectedProperty("portalMappings", Property.class);
        addProtectedProperty("writeProtection");
        addProtectedProperty("showSelfDisclosureQuestions");
        addProtectedProperty("preferences");
        addProtectedProperty("followups");
        addProtectedProperty("status");
        addProtectedProperty("note");
        addProtectedProperty("propertyManager");

        addNullProtectedProperty("region", Address.class);
    }

    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private final WorkerFileService workerFileService;

    private final AbstractImageService imageResizeService;

    private final BasePropertyRepository propertyRepository;

    private final BaseLandlordCustomerRepository customerRepository;

    private final LandlordUserRepository userRepository;

    private final BasePriosetRepository priosetRepository;

    private final BaseImportLogRepository importLogRepository;

    private final ImporterPropertyMessageSender messageSender;

    private final AttachmentHandler attachmentHandler;

    private final ImporterWorkerIndexingSenderService importerWorkerIndexingService;

    private final ImporterWorkerLocationService locationService;

    @Autowired
    public WorkerService(
            WorkerFileService workerFileService,
            AbstractImageService imageResizeService,
            BasePropertyRepository propertyRepository,
            BaseLandlordCustomerRepository customerRepository,
            LandlordUserRepository userRepository,
            BasePriosetRepository priosetRepository,
            BaseImportLogRepository importLogRepository,
            ImporterPropertyMessageSender messageSender,
            AttachmentHandler attachmentHandler,
            ImporterWorkerIndexingSenderService importerWorkerIndexingService,
            ImporterWorkerLocationService locationService
    ) {
        this.workerFileService = workerFileService;
        this.imageResizeService = imageResizeService;
        this.propertyRepository = propertyRepository;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.priosetRepository = priosetRepository;
        this.importLogRepository = importLogRepository;
        this.messageSender = messageSender;
        this.attachmentHandler = attachmentHandler;
        this.importerWorkerIndexingService = importerWorkerIndexingService;
        this.locationService = locationService;
    }

    private static void addProtectedProperty(String name) {
        try {
            protectedProperties.add(new PropertyDescriptorBean(new PropertyDescriptor(name, Property.class)));
        } catch (IntrospectionException ignored) {
            log.warn(ignored.getMessage(), ignored);
        }
    }

    private static void addNullProtectedProperty(String name, Class clazz) {
        try {
            nullProtectedProperties.add(new PropertyDescriptorBean(new PropertyDescriptor(name, clazz)));
        } catch (IntrospectionException ignored) {
            log.warn(ignored.getMessage(), ignored);
        }
    }

    private static Property updateProperty(Property source, Property target, ImportAttributes importAttributes) {

        setCountFields(source);
        setCountFields(target);

        updateProperties(source, target, importAttributes);

        return target;
    }

    private static void setCountFields(Property property) {
        property.setSizeOfApplications(0L);
        property.setSizeOfInvitees(0L);
        property.setSizeOfProposals(0L);
    }

    private static <T> void updateProperties(T source, T target, ImportAttributes importAttributes) {

        PropertyDescriptor[] pdArray;
        try {
            pdArray = Introspector.getBeanInfo(source.getClass()).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            log.error("Error updating Flatproperty -> " + source.getClass(), e);
            return;
        }

        for (PropertyDescriptor pd : pdArray) {
            if (!PropertyUtils.isWriteable(target, pd.getName()) || isPropertyUpdateExcluded(pd, importAttributes)) {
                continue;
            }

            try {
                Object sourceValue = PropertyUtils.getProperty(source, pd.getName());
                Object targetValue = PropertyUtils.getProperty(target, pd.getName());

                boolean validTarget = hasValidProperties(targetValue);

                if (isPropertyNullProtected(pd) && StringUtils.isBlank(ObjectUtils.toString(sourceValue))) {
                    continue;
                }

                if (sourceValue == null || !validTarget) {
                    PropertyUtils.setProperty(target, pd.getName(), sourceValue);
                    continue;
                }

                updateProperties(sourceValue, targetValue, importAttributes);

            } catch (Exception e) {
                log.error("Import: Error update existing Flat", e);
            }
        }
    }

    private static boolean hasValidProperties(Object obj) throws IntrospectionException {
        if (obj == null) {
            return false;
        }

        PropertyDescriptor[] pdArray = Introspector.getBeanInfo(obj.getClass()).getPropertyDescriptors();

        for (PropertyDescriptor pd : pdArray) {
            if (PropertyUtils.isWriteable(obj, pd.getName())) {
                return true;
            }
        }

        return false;
    }

    // === update existing Flat
    // =======================================================================================

    private static boolean isPropertyUpdateExcluded(PropertyDescriptor pd, ImportAttributes ia) {
        if (protectedProperties.contains(new PropertyDescriptorBean(pd))) {
            return true;
        } else {
            return ia != null && ia.isPropertyUpdateExcluded(pd);
        }

    }

    private static boolean isPropertyNullProtected(PropertyDescriptor pd) {
        return nullProtectedProperties.contains(new PropertyDescriptorBean(pd));
    }

    public void work(Message message) {
        Jackson2JsonMessageConverter jacksonMessageConverter = MessagingDefaultJackson2Serializer
                .jackson2JsonMessageConverter();
        ImportObject importObject = (ImportObject) jacksonMessageConverter.fromMessage(message);

        ImportLog importLog = null;
        if (importObject.getImportLogId() != null) {
            log.info("Using ImportLog -> " + importObject.getImportLogId());
            importLog = importLogRepository.findById(importObject.getImportLogId()).orElse(null);
        }

        if (importLog == null) {
            log.error("ABORTING: no matching importlog found with id " + importObject.getImportLogId());
            return;
        }

        LandlordCustomer customer = customerRepository.findById(importObject.getCustomerId()).orElse(null);
        if (customer == null) {
            String msg = "Importlog-ID: " + importLog.getId() + ", no such customer: " + importObject.getCustomerId();
            log.error(msg);
            reportErrorDuringImport(importLog, msg);
            return;
        }

        //CSV files by Relion didn't provided a userEmail
        LandlordUser user;
        user = getAgentUser(importObject, customer);
        if (user == null) {
            String msg = "Importlog-ID: " + importLog.getId() +
                    ", unable to load user '" + getImportMailData(importObject) +
                    "' or the CompanyAdmin for customer '" + importObject.getCustomerId() + "'.";
            log.error(msg);
            reportErrorDuringImport(importLog, msg);
            return;
        }


        S3File attachments = importObject.getAttachments();
        Property importedProperty = importObject.getProperty();

        Property existingProperty = null;
        if (importedProperty.getExternalId() != null) {
            List<Property> existingProperties = propertyRepository.findByCustomerIdAndExternalId(
                    customer.getId(), importedProperty.getExternalId().toLowerCase());

            if (!existingProperties.isEmpty()) {
                if (existingProperties.size() > 1) {
                    String msg = "Ambiguous external ID -> externalID: " + importedProperty.getExternalId()
                            + " customer: " + importedProperty.getCustomer();
                    log.error(msg);
                    reportErrorDuringImport(importLog, msg);
                    return;
                }
                existingProperty = existingProperties.get(0);

                if (existingProperty.isWriteProtected(PropertyWriteProtection.FlatWriteReason.IMPORT)) {
                    String msg = "Importlog-ID: " + importLog.getId() + ", Stop updating flat because " +
                            "writeprotection: " + importedProperty.getId() + " "
                            + existingProperty.getWriteProtection().name();
                    log.info(msg);
                    this.updateImportLog(importLog);
                    return;
                }
            }
        }

        Property property;
        boolean calculateCoordinates = true;
        boolean propertyShouldBeUpdatedOnPortals = false;

        if (!(existingProperty == null && importObject.getImportAction() == OpenImmoAktionTyp.DELETE)) {
            if (existingProperty != null) {
                log.info("Updating Flat with external ID -> " + importedProperty.getExternalId());

                deleteAttachments(existingProperty);
                calculateCoordinates = !existingProperty.getData().getAddress().addressEquals(importedProperty.getData().getAddress());
//            propertyShouldBeUpdatedOnPortals = propertyShouldBeUpdated(importedProperty, existingProperty);

                property = updateProperty(importedProperty, existingProperty, importObject.getImportAttributes());
                property.setTask(PropertyTask.IDLE);

                if (importObject.getImportAction() == OpenImmoAktionTyp.DELETE) {
                    log.info("Delete Property -> " + existingProperty.getExternalId());
                    property.setTask(PropertyTask.DELETE);
                }

                Prioset prioset = property.getPrioset();
                updatePrioset(importObject, prioset);

                if (isSameCustomerButDifferentUser(property.getUser(), user)) {
                    property.setUser(user);
                }
            } else {
                log.info("Creating new flat ...");

                property = importedProperty;

                property.setCustomer(customer);
                property.setUser(user);
                property.setShowSelfDisclosureQuestions(customer.isSelfDisclosureAllowed());
                property.setStatus(PropertyStatus.IMPORTED);

                Prioset prioset = new Prioset();
                prioset.setName("Import " + simpleDateFormat.format(new Date()));
                prioset.setCustomer(customer);
                prioset.setTemplate(false);
                PriosetData priosetData = new PriosetData();
                priosetData.setWbs(importObject.getWbs());
                prioset.setData(priosetData);
                prioset.setLocked(false);

                prioset = priosetRepository.save(prioset);

                property.setPrioset(prioset);
                property.setTask(PropertyTask.IDLE);
            }

            try {
                if (attachments != null) {
                    File downloadedFile = workerFileService.download(attachments.getUrl(), attachments.isEncrypted());
                    attachmentHandler.handleAttachmentZipFromImport(downloadedFile, property);
                }
            } catch (IOException e) {
                log.error("Could not download Import Attachment File", e);
            }

            if (property.getApplicationsViewed() == null) {
                property.setApplicationsViewed(new Date());
            }

            property.setPropertyManager(getPropertyManager(importObject, customer));

            Address address = property.getData().getAddress();
            String zipCode = address.getZipCode().trim();
            if (StringUtils.isNotBlank(zipCode)) {
                completeAddressBasedOnZip(zipCode, address);
            }

            ReflectionUtils.trimFields(property.getData());

            Property savedProperty = propertyRepository.save(property);
            log.info("imported flat saved -> " + savedProperty.getId().toString());
            indexEvent(savedProperty, customer, existingProperty == null);

            if (savedProperty.getTask() == PropertyTask.DELETE) {
                messageSender.updatePropertyOnPortals(savedProperty);
            } else {
                messageSender.createGeoCodesAndSendProposalUpdateMessage(property, calculateCoordinates);
            }
        }
        this.updateImportLog(importLog);

        try {
            if (attachments != null) {
                workerFileService.deleteImportFile(attachments);
            }
        } catch (S3FileManagerException e) {
            log.error("Could not delete Importfile: " + attachments.getUrl(), e);
        }
    }

    public boolean propertyShouldBeUpdated(Property importedProperty, Property existingProperty) {
        List<CompareBean> differences = DiffUtils.getDifferences(importedProperty.getData(), existingProperty.getData());
        return !differences.isEmpty() && differences.stream().anyMatch(compareBean ->
                !((compareBean.getFieldName().equals("address.region") && compareBean.getValue() == null) ||
                        (compareBean.getFieldName().equals("address.coordinates")) ||
                        compareBean.getFieldName().equals("attachments") ||
                        compareBean.getFieldName().equals("documents")));
    }

    private void indexEvent(Property property, LandlordCustomer customer, boolean newProperty) {
        if (newProperty) {
            importerWorkerIndexingService.propertyCreated(property, customer);
        } else {
            importerWorkerIndexingService.propertyUpdated(property, customer);
        }
    }

    private void updatePrioset(ImportObject importObject, Prioset prioset) {
        if (importObject.getWbs() != null) {
            prioset.getData().setWbs(importObject.getWbs());
            priosetRepository.save(prioset);
        }
    }

    private LandlordUser getAgentUser(ImportObject importObject, LandlordCustomer customer) {
        LandlordUser user = null;
        ImportMailData mailData = getImportMailData(importObject);

        String agentMail = mailData.getAgentMail();
        if (agentMail != null) {
            user = userRepository.findByEmailIgnoreCase(agentMail);
        }

        if (agentMail == null || user == null || !user.getCustomer().equals(customer)) {
            return userRepository.findFirstByCustomerAndUsertype(customer, LandlordUsertype.COMPANYADMIN).orElse(null);
        }
        return user;
    }

    private ImportMailData getImportMailData(ImportObject importObject) {
        return importObject.getMailData() != null ? importObject.getMailData() : new ImportMailData();
    }

    private LandlordUser getPropertyManager(ImportObject importObject, LandlordCustomer customer) {
        ImportMailData mailData = getImportMailData(importObject);

        String propertyManagerMail = mailData.getPropertyManagerMail();
        if (StringUtils.isNotBlank(propertyManagerMail)) {
            return userRepository.findByEmailIgnoreCaseAndUsertypeAndCustomer(propertyManagerMail, LandlordUsertype.PROPERTYMANAGER, customer);
        }
        return null;
    }

    private void deleteAttachments(Property property) {
        List<S3File> attachmentsToDelete = property.getData().getAttachments().stream()
                .filter(Objects::nonNull)
                .filter(s3File -> {
                    List<Property> usingAttachment = propertyRepository.findAllUsingAttachment(property.getCustomer().getId(),
                            s3File.getIdentifier());
                    return usingAttachment.stream().allMatch(property::equals);
                }).collect(Collectors.toList());

        for (S3File attachment : attachmentsToDelete) {
            imageResizeService.deleteSizes(attachment.getType(), attachment.getIdentifier(), attachment.getExtension());
        }

        workerFileService.deleteImages(attachmentsToDelete);
        workerFileService.deleteDocuments(property.getData().getDocuments());
    }

    private synchronized void updateImportLog(ImportLog importLog) {
        Optional<ImportLog> importLogOptional = importLogRepository.findById(importLog.getId());
        importLogOptional.ifPresent(oneImportLog -> {
            int current = oneImportLog.getCurrent();
            int total = oneImportLog.getTotal();

            current = current + 1;

            if (total - current <= 0 && !oneImportLog.getStatus().equals(ImportStatus.ERROR)) {
                oneImportLog.setStatus(ImportStatus.COMPLETED);
            }
            oneImportLog.setCurrent(current);
            importLogRepository.save(oneImportLog);
        });

    }

    private void reportErrorDuringImport(ImportLog importLog, String errormessage) {
        importLog.setStatus(ImportStatus.ERROR);
        importLog.extendErrorMessage(errormessage);
        importLog.setCurrent(importLog.getCurrent() + 1);

        importLogRepository.save(importLog);
    }

    private void completeAddressBasedOnZip(String zipCode, Address addressToComplete) {
        List<District> districtSet = locationService.getDistrictByZip(zipCode);

        if (!districtSet.isEmpty()) {
            District district = districtSet.get(0);
            City city = district.getCity();
            addressToComplete.setCity(city.getName());
            addressToComplete.setRegion(city.getState().getName());

            if (districtSet.size() == 1) {
                addressToComplete.setDistrict(district.getName());
            }

        } else {
            log.error("Cannot verify Address: ZipCode is invalid: " + zipCode + "\n Using Imported Address:" + addressToComplete.toString());
        }
    }

    private boolean isSameCustomerButDifferentUser(LandlordUser source, LandlordUser target) {
        return target != null && !source.getId().equals(target.getId()) &&
                source.getCustomer().getId().equals(target.getCustomer().getId());
    }

}
