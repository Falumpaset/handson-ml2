package de.immomio.broker.service.property.portal;

import de.immomio.broker.error.CustomBrokerErrorHandler;
import de.immomio.broker.helper.PortalStateContainer;
import de.immomio.broker.queue.listener.additionalLogic.property.AbstractAdditionalLogic;
import de.immomio.cloud.amazon.s3.LandlordS3FileManager;
import de.immomio.cloud.service.LandlordImageService;
import de.immomio.common.amazon.s3.S3FileManagerException;
import de.immomio.data.base.type.customer.LandlordCustomerPreference;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.base.type.property.PropertyTask;
import de.immomio.data.landlord.bean.property.Contact;
import de.immomio.data.landlord.entity.credential.Credential;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.product.LandlordProduct;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.portal.PropertyPortal;
import de.immomio.data.landlord.entity.property.portal.PropertyPortalState;
import de.immomio.data.landlord.entity.property.publish.PublishLog;
import de.immomio.data.landlord.entity.property.publish.PublishState;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.exporter.AbstractExporter;
import de.immomio.exporter.exception.ExporterException;
import de.immomio.model.repository.core.landlord.customer.property.BasePropertyRepository;
import de.immomio.model.repository.core.landlord.customer.property.portal.BaseLandlordPropertyPortalRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class PropertyPortalPublishService  {

    private final CustomBrokerErrorHandler executeErrorHandler;

    private final BasePropertyRepository basePropertyRepository;

    private final BaseLandlordPropertyPortalRepository baseLandlordPropertyPortalRepository;

    private final List<AbstractExporter> exporters;

    private final LandlordS3FileManager landlordS3FileManager;

    private final LandlordImageService landlordImageService;

    public PropertyPortalPublishService(
            CustomBrokerErrorHandler executeErrorHandler,
            BasePropertyRepository basePropertyRepository,
            BaseLandlordPropertyPortalRepository baseLandlordPropertyPortalRepository,
            List<AbstractExporter> exporters, LandlordS3FileManager landlordS3FileManager,
            LandlordImageService landlordImageService
    ) {
        this.executeErrorHandler = executeErrorHandler;
        this.basePropertyRepository = basePropertyRepository;
        this.baseLandlordPropertyPortalRepository = baseLandlordPropertyPortalRepository;
        this.exporters = exporters;
        this.landlordS3FileManager = landlordS3FileManager;
        this.landlordImageService = landlordImageService;
    }


    public void activateFlat(Property property, PublishLog publishLog) {
        Map<Portal, PortalStateContainer> result;
        result = activate(property);

        updatePortalStates(property, result);

        handleErrorState(property, result, publishLog, "Error activating/updating on one or more Portals of the property");
        setIdleState(property);
        log.info("Activating/Updating " + property.getData().getName() + " finished ...");
    }

    public void deactivateFlat(Property property, PublishLog publishLog) {
        Map<Portal, PortalStateContainer> result;
        LandlordCustomer customer = property.getCustomer();
        result = deactivateFlat(property);

        AbstractAdditionalLogic.doLogic(customer, LandlordCustomerPreference.FLAT_AFTER_DEACTIVATE, property);

        updatePortalStates(property, result);

        handleErrorState(property, result, publishLog, "Error deactivating on one or more Portals of the property");
        setIdleState(property);
        log.info("Deactivating " + property.getData().getName() + " finished ...");
    }


    public void deleteFlat(Property property, PublishLog publishLog) {
        Map<Portal, PortalStateContainer> result;
        if (!allPortalsDeactivatedCheck(property.getPortals())) {
            result = deactivateFlat(property);
            AbstractAdditionalLogic.doLogic(property.getCustomer(), LandlordCustomerPreference.FLAT_AFTER_DEACTIVATE,
                    property);
            if (!result.isEmpty() && !allPortalsDeactivatedCheck(result)) {
                updateStateAfter(property.getPortals(), result);
                setErrorState(publishLog, executeErrorHandler.handleError(property, result)); //TODO
                setIdleState(property);
                log.info("Error deleting " + property.getData().getName() + " finished ...");
                return;
            }
            if (!result.isEmpty() && allPortalsDeactivatedCheck(result)) {
                deleteProperty(property);
            }
        } else {
            deleteProperty(property);

        }

    }

    public void handleErrorState(Property property, PublishLog publishLog, Exception e) {
        log.error(e.getMessage(), e);
        setErrorState(publishLog, ExceptionUtils.getStackTrace(e));

        basePropertyRepository.findById(property.getId())
                .ifPresent(this::setIdleState);
    }

    private void updatePortalStates(Property property, Map<Portal, PortalStateContainer> result) {
        if (!result.isEmpty()) {
            updateStateAfter(property.getPortals(), result);
        }
    }

    private void setIdleState(Property property) {
        updatePropertyState(property, PropertyTask.IDLE);
    }

    private void handleErrorState(Property property, Map<Portal, PortalStateContainer> result, PublishLog publishLog, String s) {
        if (atLeastOnePortalWentIntoState(result, PropertyPortalState.ERROR)) {
            log.error(s);
            setErrorState(publishLog, executeErrorHandler.handleError(property, result)); //TODO
        }
    }

    private PublishLog setErrorState(PublishLog publishLog, String error) {
        if (publishLog.getPublishState() != PublishState.ERROR) {
            publishLog.setError(error);
            publishLog.setPublishState(PublishState.ERROR);
        }

        return publishLog;
    }

    private void updatePropertyState(Property property, PropertyTask propertyTask) {
        property.setTask(propertyTask);
        basePropertyRepository.save(property);
    }

    private void deleteProperty(Property property) {
        deleteAttachments(property);
        basePropertyRepository.deleteById(property.getId());
        log.info("Deleting " + property.getData().getName() + " finished ...");
    }

    private void deleteAttachments(Property property) {
        List<S3File> attachments = property.getData().getAttachments();
        List<String> identifiers = attachments.stream().map(S3File::getIdentifier).collect(Collectors.toList());
        List<S3File> attachmentsToDelete = new ArrayList<>();
        for (String identifier : identifiers) {
            List<Property> propertiesWithAttachment = basePropertyRepository.findAllUsingAttachment(property.getCustomer().getId(), identifier);
            boolean attachmentIsUsed = propertiesWithAttachment.stream().anyMatch(obj -> !property.equals(obj));
            if (!attachmentIsUsed) {
                attachmentsToDelete.add(attachments.stream()
                        .filter(s3File -> s3File.getIdentifier().equals(identifier))
                        .findFirst()
                        .orElse(null));
            }
        }
        attachmentsToDelete.forEach(file -> {
            try {
                landlordImageService.deleteSizes(file.getType(), file.getIdentifier(), file.getExtension());
                landlordS3FileManager.deleteImage(file.getType(), file.getIdentifier(), file.getExtension());
            } catch (S3FileManagerException e) {
                log.error(e.getMessage(), e);
            }
        });
    }

    private boolean allPortalsDeactivatedCheck(List<PropertyPortal> portals) {
        for (PropertyPortal portal : portals) {
            if (!portal.getState().equals(PropertyPortalState.DEACTIVATED)) {
                return false;
            }
        }
        return true;
    }

    private boolean allPortalsDeactivatedCheck(Map<Portal, PortalStateContainer> result) {
        for (Map.Entry<Portal, PortalStateContainer> entry : result.entrySet()) {
            PortalStateContainer value = entry.getValue();
            if (!value.getState().equals(PropertyPortalState.DEACTIVATED)) {
                return false;
            }
        }
        return true;
    }

    private Map<Portal, PortalStateContainer> activate(Property property) {
        Map<Portal, PortalStateContainer> result = activateUpdateFlat(property);

        if (property.getTask() == PropertyTask.ACTIVATE) {
            AbstractAdditionalLogic.doLogic(property.getCustomer(), LandlordCustomerPreference.FLAT_AFTER_ACTIVATE, property);
        }

        return result;
    }

    private boolean atLeastOnePortalWentIntoState(Map<Portal, PortalStateContainer> result, PropertyPortalState state) {
        for (Map.Entry<Portal, PortalStateContainer> entry : result.entrySet()) {
            if (entry.getValue().getState().equals(state)) {
                return true;
            }
        }
        return false;
    }

    private void updateState(List<PropertyPortal> portals, PropertyPortalState state) {
        if (portals != null && !portals.isEmpty()) {
            portals.forEach(portal -> portal.setState(state));
            baseLandlordPropertyPortalRepository.saveAll(portals);
        }
    }

    private void updateStateAfter(List<PropertyPortal> portals, Map<Portal, PortalStateContainer> result) {
        if (portals != null && !portals.isEmpty()) {
            portals.forEach(portal -> portal.setState(result.get(portal.getPortal()).getState()));
            baseLandlordPropertyPortalRepository.saveAll(portals);
        }
    }

    private Map<Portal, PortalStateContainer> activateUpdateFlat(
            Property property
    ) {
        List<Credential> credentials = getCredentials(property);
        Map<Portal, PortalStateContainer> result = new HashMap<>();

        for (Credential credential : credentials) {
            AbstractExporter exporter = getExporter(credential.getPortal());

            if (exporter != null) {
                boolean success = false;

                if (property.getTask() == PropertyTask.ACTIVATE) {
                    log.info("Start activating flat ... ");
                    try {
                        success = exporter.activate(property, credential);
                    } catch (ExporterException ex) {
                        log.error("an error happened during activation: " + ex.getMessage(), ex);
                        populateErrorToResult(credential.getPortal(), ex, result);
                    }
                } else if (property.getTask() == PropertyTask.UPDATE) {
                    log.info("Start updating flat ... ");
                    try {
                        success = exporter.update(property, credential);
                    } catch (ExporterException ex) {
                        populateErrorToResult(credential.getPortal(), ex, result);
                    }
                } else {
                    log.info("No operation found for flat");
                    continue;
                }

                if (success) {
                    PortalStateContainer val = new PortalStateContainer(PropertyPortalState.ACTIVE, null);
                    result.put(credential.getPortal(), val);
                }
            } else {
                log.info("Found no exporter: " + credential.getPortal());
            }
        }

        return result;
    }

    private LandlordProduct getProduct(Property property) {
        return property.getCustomer().getActiveProduct().getProduct();
    }

    private void populateErrorToResult(Portal portal, ExporterException ex, Map<Portal, PortalStateContainer> result) {
        PortalStateContainer val = new PortalStateContainer(PropertyPortalState.ERROR, ex);
        result.put(portal, val);
    }

    private Map<Portal, PortalStateContainer> deactivateFlat(Property property) {

        List<Credential> credentials = getCredentials(property);
        Map<Portal, PortalStateContainer> result = new HashMap<>();

        log.info("Deactivate portals for flat " + property.getId().toString());

        for (Credential credential : credentials) {
            AbstractExporter exporter = getExporter(credential.getPortal());

            if (exporter != null) {
                boolean success = false;

                try {
                    success = exporter.deactivate(property, credential);
                } catch (ExporterException ex) {
                    log.info("Exception deactivating portal2 -> " + credential.getPortal().name(), ex);
                    populateErrorToResult(credential.getPortal(), ex, result);
                }

                if (success) {
                    log.info("Portal deactivate -> " + credential.getPortal().name());
                    PortalStateContainer val = new PortalStateContainer(PropertyPortalState.DEACTIVATED, null);
                    result.put(credential.getPortal(), val);
                    property.setAutoOfferEnabled(false);
                }
            } else {
                log.info("Exporter not found -> " + credential.getPortal().name());
            }
        }

        return result;
    }

    private Map<Portal, PortalStateContainer> deleteFlat(Property property) {
        return deactivateFlat(property);
    }

    private List<Credential> getCredentials(Property property) {
        List<Credential> credentials = new ArrayList<>();

        for (PropertyPortal portal : property.getPortals()) {
            Credential credential = portal.getCredentials();

            if (credential != null && !credential.getCustomer().getId().equals(property.getCustomer().getId())) {
                log.error("Error portal2-credential: " + credential.getPortal() + " (" + portal
                        + ") Credential-CID: " + credential.getCustomer().getId() + " Flat-CID: "
                        + property.getCustomer().getId());
                continue;
            }
            credentials.add(credential);
        }

        return credentials;
    }

    private AbstractExporter getExporter(Portal portal) {
        for (AbstractExporter exporter : exporters) {
            if (exporter.isApplicable(portal)) {
                log.debug("searching exporter for portal `" + portal + "` -> " + exporter.getClass().getSimpleName()
                        + " [true]");
                return exporter;
            }
            log.debug("searching exporter for portal `" + portal + "` -> " + exporter.getClass().getSimpleName()
                    + " [false]");
        }

        return null;
    }

}
