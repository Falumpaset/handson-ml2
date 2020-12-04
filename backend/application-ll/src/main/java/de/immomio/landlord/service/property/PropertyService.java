package de.immomio.landlord.service.property;

import de.immomio.beans.landlord.UpdatePropertyBean;
import de.immomio.beans.shared.conversation.ConversationConfigBean;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.ImmomioRuntimeException;
import de.immomio.constants.exceptions.NotAuthorizedException;
import de.immomio.controller.search.property.SearchProperty;
import de.immomio.controller.search.property.SearchPropertyAddress;
import de.immomio.controller.search.property.SearchPropertyPortalState;
import de.immomio.data.base.type.property.ObjectType;
import de.immomio.data.base.type.property.PropertyStatus;
import de.immomio.data.base.type.property.PropertyTask;
import de.immomio.data.base.type.property.PropertyType;
import de.immomio.data.landlord.bean.property.AvailableFrom;
import de.immomio.data.landlord.bean.property.certificate.DemandCertificate;
import de.immomio.data.landlord.bean.property.certificate.EnergyCertificate;
import de.immomio.data.landlord.bean.property.certificate.UsageCertificate;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.bean.user.AgentInfo;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.prioset.Prioset;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.note.PropertyNoteBean;
import de.immomio.data.landlord.entity.property.portal.PropertyPortal;
import de.immomio.data.landlord.entity.property.portal.PropertyPortalState;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.shared.bean.common.DoubleRange;
import de.immomio.data.shared.bean.common.ParkingSpace;
import de.immomio.data.shared.entity.property.tenant.PropertyTenant;
import de.immomio.landlord.service.prioset.PriosetService;
import de.immomio.landlord.service.property.notification.PropertyNotificationService;
import de.immomio.landlord.service.property.portal.PropertyPortalService;
import de.immomio.landlord.service.reporting.indexing.delegate.LandlordPropertyIndexingDelegate;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.landlord.service.sender.LandlordProposalMessageSender;
import de.immomio.model.repository.core.shared.propertyProposal.BasePropertyProposalRepository;
import de.immomio.model.repository.landlord.customer.user.LandlordUserRepository;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.model.repository.shared.appointment.AppointmentRepository;
import de.immomio.model.repository.shared.property.PropertyRepository;
import de.immomio.model.repository.shared.tenant.PropertyTenantRepository;
import de.immomio.service.RabbitMQService;
import de.immomio.service.conversation.ConversationConfigService;
import de.immomio.utils.ReflectionUtils;
import de.immomio.utils.criteria.CriteriaQueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.criteria.internal.OrderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static de.immomio.data.landlord.bean.property.certificate.EnergyCertificate.EnergyCertificateType.DEMAND_IDENTIFICATION;
import static de.immomio.data.landlord.bean.property.certificate.EnergyCertificate.EnergyCertificateType.USAGE_IDENTIFICATION;
import static de.immomio.utils.criteria.CriteriaQueryUtils.getJsonOrder;
import static de.immomio.utils.criteria.CriteriaQueryUtils.jsonFunction;
import static de.immomio.utils.criteria.CriteriaQueryUtils.populateJsonbBetweenPredicate;
import static de.immomio.utils.criteria.CriteriaQueryUtils.populateJsonbEqualPredicate;
import static de.immomio.utils.criteria.CriteriaQueryUtils.populateJsonbLikePredicate;

@Slf4j
@Service
public class PropertyService {

    public static final String PARKING_SPACE_PRICE_OR_COUNT_MISSING = "PARKING_SPACE_PRICE_OR_COUNT_MISSING_L";
    public static final String CREATION_DATE_NOT_SET = "CREATION_DATE_NOT_SET_L";
    public static final String ENERGY_EFFICIENCY_CLASS_NOT_SET_L = "ENERGY_EFFICIENCY_CLASS_NOT_SET_L";
    public static final String PROPERTY_EXTERNAL_ID_CONFLICT_L = "PROPERTY_EXTERNAL_ID_CONFLICT_L";
    public static final String PROPERTY_CONVERSATION_CONFIG_MISSING_L = "PROPERTY_CONVERSATION_CONFIG_MISSING_L";
    public static final String PROPERTY_WRITE_PROTECTION_L = "PROPERTY_WRITE_PROTECTION_L";
    public static final String PROPERTY_TYPE_MISSING_L = "PROPERTY_TYPE_MISSING_L";
    public static final String PROPERTY_USER_ID_MISSING_L = "PROPERTY_USER_ID_MISSING_L";
    public static final String AUTO_OFFER_THRESHOLD_NOT_IN_RANGE_L = "AUTO_OFFER_THRESHOLD_NOT_IN_RANGE_L";
    public static final String PROPERTY_NOT_FLAT_PRIOSET_SHOULD_BE_NULL_L = "PROPERTY_NOT_FLAT_PRIOSET_SHOULD_BE_NULL_L";
    public static final String PROPERTY_FLAT_PRIOSET_MISSING_L = "PROPERTY_FLAT_PRIOSET_MISSING_L";
    public static final String USER_IS_NO_PROPERTY_MANAGER_L = "USER_IS_NO_PROPERTY_MANAGER_L";
    public static final String PROPERTY_MANAGER_NO_FOUND_L = "PROPERTY_MANAGER_NO_FOUND_L";
    private static final String DATA = "data";
    private static final String BASEPRICE = "basePrice";
    private static final String ROOMS = "rooms";
    private static final String SIZE = "size";
    private static final String NAME = "name";
    private static final String EXTERNAL_ID = "externalId";
    private static final String USER = "user";
    private static final String PRIOSET = "prioset";
    private static final String ID = "id";
    private static final String WBS = "wbs";
    private static final String CUSTOMER = "customer";
    private static final String TENANT = "tenant";
    private static final String ADDRESS_CITY = "address.city";
    private static final String ADDRESS_ZIP_CODE = "address.zipCode";
    private static final String ADDRESS_STREET = "address.street";
    private static final String ADDRESS_REGION = "address.region";
    private static final String ADDRESS_COUNTRY = "address.country";
    private static final String ADDRESS_HOUSE_NUMBER = "address.houseNumber";
    private static final String REGION_NOT_SET = "REGION_NOT_SET_L";
    private static final String ENDENERGYCONSUMPTION_NOT_SET = "ENDENERGYCONSUMPTION_NOT_SET_L";
    private static final String STATUS = "status";
    private static final String TYPE = "type";
    private static final String PROPERTY = "property";
    private static final String STATE = "state";
    private static final String JSONB_FIELD_SEPARATOR = "->>";
    private static final String ACCESS_NOT_ALLOWED_L = "ACCESS_NOT_ALLOWED_L";
    private static final String ERROR_WHILE_MAKING_FLAT_AVAILABLE_FOR_RENT = "ERROR_WHILE_MAKING_FLAT_AVAILABLE_FOR_RENT_L";
    private static final String FLAT_IS_NOT_RENTED = "FLAT_IS_NOT_RENTED_L";
    private static final double MAX_THRESHOLD = 10d;
    private static final double MIN_THRESHOLD = 0d;
    private static final String CONVERSATION_CONFIGS = "conversationConfigs";
    private static final String PROPERTYMANAGER = "propertyManager";
    private static final String AVAILABLE_FROM_SORTING = "data->>availableFrom->>dateAvailable";
    private static final String MINIMUM_DATE = "0001-01-01";
    private static final String AVAILABLE_FROM_STRING_TO_LONG_L = "AVAILABLE_FROM_STRING_TO_LONG_L";
    private static final int AVAILABLE_FROM_STRING_MAX_SIZE = 50;
    private final EntityManager entityManager;

    private final PropertyCountService propertyCountService;

    private final PropertyRepository propertyRepository;

    private final PropertyTenantRepository propertyTenantRepository;

    private final UserSecurityService userSecurityService;

    private final AppointmentRepository appointmentRepository;

    private final PropertyApplicationRepository applicationRepository;

    private final BasePropertyProposalRepository proposalRepository;

    private final LandlordProposalMessageSender landlordProposalMessageSender;

    private final LandlordPropertyIndexingDelegate propertyIndexingDelegate;

    private final ConversationConfigService conversationConfigService;

    private final RabbitMQService rabbitMQService;

    private final PriosetService priosetService;

    private final LandlordUserRepository userRepository;

    private final PropertyPortalService propertyPortalService;

    private final PropertyNotificationService propertyNotificationService;

    @Autowired
    public PropertyService(EntityManager entityManager,
            PropertyCountService propertyCountService,
            PropertyRepository propertyRepository,
            UserSecurityService userSecurityService,
            AppointmentRepository appointmentRepository,
            LandlordProposalMessageSender landlordProposalMessageSender,
            PropertyTenantRepository propertyTenantRepository,
            PropertyApplicationRepository applicationRepository,
            BasePropertyProposalRepository proposalRepository,
            LandlordPropertyIndexingDelegate propertyIndexingDelegate,
            ConversationConfigService conversationConfigService,
            RabbitMQService rabbitMQService,
            PriosetService priosetService,
            LandlordUserRepository userRepository,
            PropertyPortalService propertyPortalService,
            PropertyNotificationService propertyNotificationService
    ) {
        this.entityManager = entityManager;
        this.propertyCountService = propertyCountService;
        this.propertyRepository = propertyRepository;
        this.userSecurityService = userSecurityService;
        this.appointmentRepository = appointmentRepository;
        this.landlordProposalMessageSender = landlordProposalMessageSender;
        this.propertyTenantRepository = propertyTenantRepository;
        this.applicationRepository = applicationRepository;
        this.proposalRepository = proposalRepository;
        this.propertyIndexingDelegate = propertyIndexingDelegate;
        this.conversationConfigService = conversationConfigService;
        this.rabbitMQService = rabbitMQService;
        this.priosetService = priosetService;
        this.userRepository = userRepository;
        this.propertyPortalService = propertyPortalService;
        this.propertyNotificationService = propertyNotificationService;
    }

    @Transactional(readOnly = true)
    public Page<Property> search(SearchProperty searchProperty, LandlordCustomer customer, PageRequest pageRequest) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Property> query = builder.createQuery(Property.class);
        Root<Property> root = query.from(Property.class);

        List<Order> orders = CriteriaQueryUtils.generateSortOrders(builder, root, pageRequest, (sortOrder) -> {
            if (sortOrder.getProperty().contains(AVAILABLE_FROM_SORTING)) {
                boolean ascending = sortOrder.getDirection() == Sort.Direction.ASC;
                String[] paths = sortOrder.getProperty().split("\\" + JSONB_FIELD_SEPARATOR);
                String fieldName = paths[0];
                String jsonElement = getJsonOrder(paths);
                return new OrderImpl(builder.coalesce(jsonFunction(builder, root, fieldName, jsonElement), MINIMUM_DATE), ascending);
            }
            return null;
        });

        Predicate predicate = createPredicate(customer, searchProperty, builder, query, root);
        int offset = Long.valueOf(pageRequest.getOffset()).intValue();
        List<Property> properties = entityManager.createQuery(query.where(predicate).orderBy(orders))
                .setFirstResult(offset)
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();

        propertyCountService.populatePropertiesWithCountData(properties);

        return new PageImpl<>(properties, pageRequest, getCount(builder, searchProperty, customer));
    }

    public Property findById(Long id) throws NotAuthorizedException {
        Property property = propertyRepository.findById(id).orElse(null);

        if (property == null) {
            return null;
        }

        Long customerId = userSecurityService.getPrincipalUser().getCustomer().getId();
        if (!property.getCustomer().getId().equals(customerId)) {
            throw new NotAuthorizedException(ACCESS_NOT_ALLOWED_L);
        }

        propertyCountService.populatePropertyWithCountData(property);
        return property;
    }

    public boolean isExternalIdAvailableForCustomerAndProperty(String externalId, Property property) {
        if (property != null && externalId.equals(property.getExternalId())) {
            return true;
        }

        var customer = userSecurityService.getPrincipalUser().getCustomer();
        return !propertyRepository.existsByCustomerAndExternalId(customer, externalId);
    }

    public Property createProperty(UpdatePropertyBean updatePropertyBean, LandlordCustomer customer) {
        validatePropertyBean(updatePropertyBean);

        Prioset prioset = null;
        if (updatePropertyBean.getType() == PropertyType.FLAT) {
            prioset = priosetService.create(updatePropertyBean.getPrioset(), customer);
        }

        Property property = new Property();
        property.setCustomer(customer);
        if (updatePropertyBean.getExternalId() != null) {
            property.setExternalId(updatePropertyBean.getExternalId());
        }
        property.setTask(PropertyTask.IDLE);

        property = updateProperty(property, updatePropertyBean, prioset);

        propertyIndexingDelegate.propertyCreated(property);

        return property;
    }

    public Property updateProperty(UpdatePropertyBean updateProperty, Property property) {
        validatePropertyBean(updateProperty);

        Prioset prioset = null;
        if (updateProperty.getType() == PropertyType.FLAT && updateProperty.getPrioset() != null) {
            prioset = priosetService.update(property.getPrioset(), updateProperty.getPrioset());
        }

        property = updateProperty(property, updateProperty, prioset);

        propertyIndexingDelegate.propertyUpdated(property);

        return property;
    }

    public void validateProperty(Property property) {
        PropertyData data = property.getData();
        if (StringUtils.isEmpty(data.getAddress().getRegion())) {
            throw new ApiValidationException(REGION_NOT_SET);
        }

        validateAvailableFrom(data);

        List<ParkingSpace> parkingSpaces = data.getParkingSpaces();
        if (parkingSpaces != null) {
            boolean invalidParkingSpace = parkingSpaces.stream().anyMatch(parkingSpace ->
                    parkingSpace.getType() != null &&
                            (parkingSpace.getPrice() == null ||
                                    (parkingSpace.getCount() == 0 || parkingSpace.getCount() == null)));

            if (invalidParkingSpace) {
                throw new ApiValidationException(PARKING_SPACE_PRICE_OR_COUNT_MISSING);
            }
        }

        EnergyCertificate energyCertificate = data.getEnergyCertificate();
        if (!Arrays.asList(EnergyCertificate.CertificateCreationDate.NOT_NECESSARY,
                EnergyCertificate.CertificateCreationDate.WITHOUT)
                .contains(energyCertificate.getCreationDate())) {
            if (energyCertificate.getEnergyCertificateType() == DEMAND_IDENTIFICATION) {
                if (energyCertificate.getCreationDate() == null) {
                    throw new ApiValidationException(CREATION_DATE_NOT_SET);
                }
                DemandCertificate demandCertificate = energyCertificate.getDemandCertificate();
                if (StringUtils.isEmpty(demandCertificate.getEndEnergyConsumption())) {
                    throw new ApiValidationException(ENDENERGYCONSUMPTION_NOT_SET);
                }
                if (energyCertificate.getCreationDate() == EnergyCertificate.CertificateCreationDate.MAY_2014) {
                    if (StringUtils.isEmpty(demandCertificate.getEnergyEfficiencyClass())) {
                        throw new ApiValidationException(ENERGY_EFFICIENCY_CLASS_NOT_SET_L);
                    }
                }
            }

            if (energyCertificate.getEnergyCertificateType() == USAGE_IDENTIFICATION) {
                if (energyCertificate.getCreationDate() == null) {
                    throw new ApiValidationException(CREATION_DATE_NOT_SET);
                }
                UsageCertificate usageCertificate = energyCertificate.getUsageCertificate();
                if (StringUtils.isEmpty(usageCertificate.getEnergyConsumption())) {
                    throw new ApiValidationException(ENDENERGYCONSUMPTION_NOT_SET);
                }
                if (energyCertificate.getCreationDate() == EnergyCertificate.CertificateCreationDate.MAY_2014) {
                    if (usageCertificate.getEnergyEfficiencyClass() == null) {
                        throw new ApiValidationException(ENERGY_EFFICIENCY_CLASS_NOT_SET_L);
                    }
                }
            }
        }
    }

    public void setPropertyNote(Property property, String note) {
        PropertyNoteBean propertyNote = property.getNote();
        propertyNote.setContent(note);
        propertyNote.setAgentInfo(new AgentInfo(userSecurityService.getPrincipalUser()));
        propertyNote.setUpdated(new Date());

        propertyRepository.save(property);
    }

    public Map<String, Long> countPropertyApplications(Map<Long, Date> model) {
        Map<String, Long> countByApplications = new HashMap<>();

        model.forEach((propertyId, lastQueriedDate) -> {
            if (userSecurityService.allowUserToReadProperty(propertyId)) {
                Long applicationCount;
                if (lastQueriedDate != null) {
                    applicationCount = applicationRepository.countByPropertyAfterDate(propertyId, lastQueriedDate);
                } else {
                    applicationCount = propertyCountService.getApplicationCount(propertyId);
                }
                countByApplications.put(propertyId.toString(), applicationCount);
            }
        });

        return countByApplications;
    }

    public void setConversationConfigs(ConversationConfigBean configBean, Property property) {
        if (configBean != null) {
            Map<String, String> propertyPreferences = property.getPreferences();
            String serializedConfigs = conversationConfigService.serializeConfigs(configBean);
            propertyPreferences.put(CONVERSATION_CONFIGS, serializedConfigs);
        }
    }

    public void resetProperty(Property property, boolean resetApplications, boolean deleteAppointments) {
        checkRentStatus(property);

        try {
            PropertyTenant tenant = property.getTenant();
            propertyTenantRepository.delete(tenant);
            updatePropertyState(property, PropertyStatus.DEFAULT);

            if (resetApplications) {
                applicationRepository.resetApplications(property.getId());
                proposalRepository.resetProposals(property.getId());
            }

            if (deleteAppointments) {
                appointmentRepository.deleteAll(property.getAppointments());
            }
            landlordProposalMessageSender.sendProposalUpdateMessage(property, false);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            throw new ImmomioRuntimeException(ERROR_WHILE_MAKING_FLAT_AVAILABLE_FOR_RENT);

        }

    }

    public void updatePropertyStates(List<Long> propertyIds, PropertyStatus statusToSet) {
        var customer = userSecurityService.getPrincipalUser().getCustomer();

        propertyRepository.updateStatesByIdsAndUser(propertyIds, customer, statusToSet);
    }

    public void updatePropertyState(Property property, PropertyStatus statusToSet) {
        property.setStatus(statusToSet);
        propertyRepository.save(property);
    }

    public List<Property> duplicateProperties(Property property, Integer amount) {
        List<Property> propertiesToCreate = new ArrayList<>();
        IntStream.range(0, amount).forEach(value -> {
            Property newProperty = duplicateProperty(property);
            propertiesToCreate.add(newProperty);
        });

        List<Property> savedProperties = propertyRepository.saveAll(propertiesToCreate);
        savedProperties.forEach(oneProperty -> {
            landlordProposalMessageSender.sendProposalUpdateMessage(oneProperty, true);
            propertyIndexingDelegate.propertyCreated(oneProperty);
        });

        return savedProperties;
    }

    public boolean isOnePortalActivated(List<Property> properties) {
        return properties.stream()
                .map(Property::getPortals)
                .flatMap(Collection::stream)
                .anyMatch(propertyPortal -> propertyPortal.getState() != PropertyPortalState.DEACTIVATED);
    }

    public void setShowSelfDisclosureQuestionsFlag(LandlordCustomer customer) {
        propertyRepository.setShowSelfDisclosureQuestionsFlag(customer.getId());
    }

    public void unsetShowSelfDisclosureQuestionsFlag(LandlordCustomer customer) {
        propertyRepository.unsetShowSelfDisclosureQuestionsFlag(customer.getId());
    }

    public void deleteAllPropertiesFromPrincipal() {
        propertyRepository.deleteFromPrincipal();
    }

    public Map<String, Long> getPropertyCountsForFilter(Map<String, SearchProperty> searchPropertyMap) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        builder.createQuery(Long.class);
        LandlordCustomer landlordCustomer = userSecurityService.getPrincipalUser().getCustomer();

        return searchPropertyMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry ->
                getCount(builder, entry.getValue(), landlordCustomer)));
    }

    private Property updateProperty(Property property, UpdatePropertyBean updatePropertyBean, Prioset newPrioset) {
        boolean newPropertyManager = false;

        if (updatePropertyBean.getExternalId() != null && !isExternalIdAvailableForCustomerAndProperty(updatePropertyBean.getExternalId(), property)) {
            throw new ApiValidationException(PROPERTY_EXTERNAL_ID_CONFLICT_L);
        }
        if (updatePropertyBean.getAutoOfferThreshold() != null && (updatePropertyBean.getAutoOfferThreshold()
                < MIN_THRESHOLD || updatePropertyBean.getAutoOfferThreshold() > MAX_THRESHOLD)) {
            throw new ApiValidationException(AUTO_OFFER_THRESHOLD_NOT_IN_RANGE_L);
        }
        if (updatePropertyBean.getConversationConfigs() != null) {
            conversationConfigService.validateConversationConfigBean(updatePropertyBean.getConversationConfigs());
        }

        if (updatePropertyBean.getPropertyManagerId() != null) {
            LandlordUser propertyManager = userRepository
                    .findByIdAndCustomerAndUsertype(updatePropertyBean.getPropertyManagerId(), property.getCustomer(), LandlordUsertype.PROPERTYMANAGER)
                    .orElseThrow(() -> new ApiValidationException(PROPERTY_MANAGER_NO_FOUND_L));

            if (!propertyManager.equals(property.getPropertyManager())) {
                newPropertyManager = true;
                property.setPropertyManager(propertyManager);
            }
        } else {
            property.setPropertyManager(null);
        }

        boolean calculateCoordinates = false;
        try {
            calculateCoordinates = property.getData() == null || !(property.getData().getAddress().addressEquals(updatePropertyBean.getData().getAddress()));
        } catch (Exception ex) {
            log.error("Don't know, if I can caluculate.", ex);
        }

        PropertyData data = updatePropertyBean.getData();
        if (data != null) {
            PropertyType propertyType = updatePropertyBean.getType();

            if (propertyType != null) {
                property.setType(updatePropertyBean.getType());
                if (data.getObjectType() == null) {
                    switch (propertyType) {
                        case GARAGE:
                            data.setObjectType(ObjectType.GARAGE);
                            break;
                        case COMMERCIAL:
                            data.setObjectType(ObjectType.OFFICE);
                            break;
                        default:
                            data.setObjectType(ObjectType.FLAT);
                    }
                }
            }
            ReflectionUtils.trimFields(data);

            property.setData(data);
        }

        if (property.getType() == PropertyType.FLAT && newPrioset != null) {
            property.setPrioset(newPrioset);
        }

        if (updatePropertyBean.getUserId() != null) {
            property.setUser(userRepository.findById(updatePropertyBean.getUserId()).orElseThrow());
        }

        if (updatePropertyBean.getWriteProtection() != null) {
            property.setWriteProtection(updatePropertyBean.getWriteProtection());
        }

        property.setAutoOfferEnabled(updatePropertyBean.isAutoOfferEnabled());
        property.setShowSelfDisclosureQuestions(updatePropertyBean.isShowSelfDisclosureQuestions());

        if (updatePropertyBean.getAutoOfferThreshold() != null) {
            property.setAutoOfferThreshold(updatePropertyBean.getAutoOfferThreshold());
        }

        setConversationConfigs(updatePropertyBean.getConversationConfigs(), property);

        if (property.getStatus() == PropertyStatus.IMPORTED) {
            property.setStatus(PropertyStatus.DEFAULT);
        }

        property.setExternalId(updatePropertyBean.getExternalId());

        if (property.isActive()) {
            property.setTask(PropertyTask.UPDATE);
        }
        property = propertyRepository.save(property);

        propertyPortalService.mergePropertyPortals(property, updatePropertyBean.getCredentialIds());
        propertyCountService.populatePropertyWithCountData(property);
        landlordProposalMessageSender.sendProposalUpdateMessage(property, calculateCoordinates);
        if (newPropertyManager) {
            propertyNotificationService.notifyNewPropertyManager(property);
        }

        return property.isActive() ? queueAndSaveActiveProperty(property) : property;
    }

    private Property queueAndSaveActiveProperty(Property property) {
        rabbitMQService.queueProperty(property);
        return property;
    }

    private Property duplicateProperty(Property property) {
        PropertyData data = property.getData();
        Property newProperty = new Property();

        newProperty.setAutoOfferEnabled(false);
        newProperty.setAutoOfferThreshold(property.getAutoOfferThreshold());
        newProperty.setCustomer(property.getCustomer());
        newProperty.setData(data);
        newProperty.setRuntimeInDays(property.getRuntimeInDays());
        newProperty.setTask(PropertyTask.IDLE);
        newProperty.setWriteProtection(property.getWriteProtection());
        newProperty.setValidUntil(property.getValidUntil());
        newProperty.setUser(property.getUser());
        newProperty.setType(property.getType());

        if (property.getType() == PropertyType.FLAT) {
            newProperty.setPrioset(priosetService.duplicatePrioset(property.getPrioset()));
        }

        return newProperty;
    }

    private Predicate createPredicate(
            LandlordCustomer customer,
            SearchProperty searchProperty,
            CriteriaBuilder builder,
            CriteriaQuery query,
            Root<Property> root
    ) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get(CUSTOMER), customer));
        LandlordUser principalUser = userSecurityService.getPrincipalUser();

        if (principalUser.getUsertype() == LandlordUsertype.PROPERTYMANAGER) {
            predicates.add(builder.equal(root.get(PROPERTYMANAGER), principalUser));
        } else if (searchProperty.getAgents() != null && !searchProperty.getAgents().isEmpty()) {
            predicates.add(root.get(USER).get(ID).in(searchProperty.getAgents()));
        }

        Boolean wbs = searchProperty.getWbs();

        populateWbsPredicate(root, builder, query, predicates, wbs);

        populateJsonbLikePredicate(builder, root, DATA, NAME, searchProperty.getName(), predicates);

        DoubleRange basePrice = searchProperty.getBasePrice();
        if (rangeNotEmpty(basePrice)) {
            populateJsonbBetweenPredicate(builder, root, DATA, BASEPRICE,
                    basePrice.getFrom(), basePrice.getTo(), predicates, Double.class);
        }

        DoubleRange roomNumber = searchProperty.getRoomNumber();
        if (rangeNotEmpty(roomNumber)) {
            populateJsonbBetweenPredicate(builder, root, DATA, ROOMS,
                    roomNumber.getFrom(), roomNumber.getTo(), predicates, Double.class);
        }
        DoubleRange size = searchProperty.getPropertySize();
        if (rangeNotEmpty(size)) {
            populateJsonbBetweenPredicate(builder, root, DATA, SIZE,
                    size.getFrom(), size.getTo(), predicates, Double.class);
        }

        populateAddressPredicates(searchProperty, builder, root, predicates);

        Boolean rented = searchProperty.getRented();
        if (rented != null && rented) {
            predicates.add(builder.exists(propertyTenantSubQuery(builder, query, root)));
            Join<Object, Object> join = root.join(TENANT, JoinType.INNER);
        }

        String externalId = searchProperty.getExternalId();
        if (!StringUtils.isEmpty(externalId)) {
            externalId = "%" + externalId + "%";
            predicates.add(builder.like(root.get(EXTERNAL_ID), externalId));
        }

        if (rented != null && !rented) {
            predicates.add(builder.not(builder.exists(propertyTenantSubQuery(builder, query, root))));
        }

        addEnumListToPredicates(searchProperty.getStatuses(), builder, root.get(STATUS), predicates);

        addEnumListToPredicates(searchProperty.getTypes(), builder, root.get(TYPE), predicates);

        SearchPropertyPortalState propertyPortalState = searchProperty.getPropertyPortal();
        if (propertyPortalState.getStates() != null && !propertyPortalState.getStates().isEmpty()) {
            Predicate withExistStates = getExistStatesPredicate(searchProperty, builder, query, root);
            if (propertyPortalState.isIncludeWithoutPortals()) {
                Predicate notExistPortals = builder.not(
                        builder.exists(propertyPortalSubQuery(searchProperty, builder, query, root, false)));
                predicates.add(builder.or(withExistStates, notExistPortals));
            } else {
                predicates.add(withExistStates);
            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }

    private <L extends Enum<L>> void addEnumListToPredicates(List<L> listToAdd, CriteriaBuilder builder, Path<L> pathToList, List<Predicate> predicates) {
        if (!(listToAdd == null || listToAdd.isEmpty())) {
            CriteriaBuilder.In<L> listInRoot = builder.in(pathToList);
            listToAdd.forEach(listInRoot::value);
            predicates.add(builder.and(listInRoot));
        }
    }

    private boolean rangeNotEmpty(DoubleRange range) {
        return range != null && (range.getFrom() != Double.MIN_VALUE || range.getTo() != Double.MAX_VALUE);
    }

    private void populateWbsPredicate(
            Root<Property> root, CriteriaBuilder builder,
            CriteriaQuery query,
            List<Predicate> predicates,
            Boolean wbs
    ) {
        if (wbs != null) {
            List<Predicate> priosetPredicates = new ArrayList<>();
            Subquery<Prioset> priosetSubquery = query.subquery(Prioset.class);
            Root<Prioset> priosetRoot = priosetSubquery.from(Prioset.class);
            priosetPredicates.add(builder.equal(root.get(PRIOSET), priosetRoot.get(ID)));

            populateJsonbEqualPredicate(builder, priosetRoot, DATA, WBS, wbs.toString(), priosetPredicates);

            priosetSubquery.select(priosetRoot).where(priosetPredicates.toArray(new Predicate[0]));
            predicates.add(builder.and(builder.exists(priosetSubquery)));
        }
    }

    private Predicate getExistStatesPredicate(
            SearchProperty searchProperty,
            CriteriaBuilder builder,
            CriteriaQuery query,
            Root<Property> root) {
        switch (searchProperty.getPropertyPortal().getPredicate()) {
            case AND:
                List<PropertyPortalState> states = searchProperty.getPropertyPortal().getStates();
                Predicate[] existStates = states.stream()
                        .map(state -> builder.exists(
                                propertyPortalWithStateSubQuery(state, builder, query, root)))
                        .toArray(Predicate[]::new);

                return builder.and(existStates);
            default:
                return builder.exists(propertyPortalSubQuery(searchProperty, builder, query, root, true));
        }
    }

    private void populateAddressPredicates(
            SearchProperty searchProperty,
            CriteriaBuilder builder,
            Root<Property> root,
            List<Predicate> predicates
    ) {
        SearchPropertyAddress address = searchProperty.getAddress();
        if (address != null) {
            populateJsonbLikePredicate(builder, root, DATA, ADDRESS_CITY, address.getCity(), predicates);
            populateJsonbLikePredicate(builder, root, DATA, ADDRESS_ZIP_CODE, address.getZipCode(), predicates);
            populateJsonbLikePredicate(builder, root, DATA, ADDRESS_STREET, address.getStreet(), predicates);
            populateJsonbLikePredicate(builder, root, DATA, ADDRESS_REGION, address.getRegion(), predicates);
            populateJsonbEqualPredicate(builder, root, DATA, ADDRESS_COUNTRY, address.getCountryString(), predicates);
            populateJsonbLikePredicate(builder, root, DATA, ADDRESS_HOUSE_NUMBER, address.getHouseNumber(),
                    predicates);
        }
    }

    private Subquery<PropertyPortal> propertyPortalSubQuery(
            SearchProperty searchProperty,
            CriteriaBuilder builder,
            CriteriaQuery query,
            Root<Property> root,
            boolean withStates
    ) {
        Subquery<PropertyPortal> subQuery = query.subquery(PropertyPortal.class);
        Root<PropertyPortal> subQueryRoot = subQuery.from(PropertyPortal.class);

        Path propertyPath = subQueryRoot.get(PROPERTY);
        if (withStates) {
            subQuery.select(subQueryRoot)
                    .where(builder.equal(propertyPath, root),
                            subQueryRoot.get(STATE).in(searchProperty.getPropertyPortal().getStates())
                    );
        } else {
            subQuery.select(subQueryRoot).where(builder.equal(propertyPath, root));
        }

        return subQuery;
    }

    private Subquery<PropertyPortal> propertyPortalWithStateSubQuery(
            PropertyPortalState state,
            CriteriaBuilder builder,
            CriteriaQuery query,
            Root<Property> root
    ) {
        Subquery<PropertyPortal> subQuery = query.subquery(PropertyPortal.class);
        Root<PropertyPortal> subQueryRoot = subQuery.from(PropertyPortal.class);

        Path propertyPath = subQueryRoot.get(PROPERTY);
        subQuery.select(subQueryRoot)
                .where(builder.equal(propertyPath, root), builder.equal(subQueryRoot.get("state"), state));

        return subQuery;
    }

    private Subquery<PropertyTenant> propertyTenantSubQuery(
            CriteriaBuilder builder,
            CriteriaQuery query,
            Root<Property> root
    ) {
        Subquery<PropertyTenant> subQuery = query.subquery(PropertyTenant.class);
        Root<PropertyTenant> subQueryRoot = subQuery.from(PropertyTenant.class);

        Path propertyPath = subQueryRoot.get(PROPERTY);
        subQuery.select(subQueryRoot).where(builder.equal(propertyPath, root));
        return subQuery;
    }

    private Long getCount(CriteriaBuilder builder, SearchProperty searchProperty, LandlordCustomer customer) {
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Property> root = query.from(Property.class);

        Predicate predicate = createPredicate(customer, searchProperty, builder, query, root);
        query.select(builder.countDistinct(root));
        return entityManager.createQuery(query.where(predicate)).getSingleResult();
    }

    private void validatePropertyBean(UpdatePropertyBean updatePropertyBean) {
        if (updatePropertyBean.getUserId() == null) {
            throw new ApiValidationException(PROPERTY_USER_ID_MISSING_L);
        }
        if (updatePropertyBean.getType() == null) {
            throw new ApiValidationException(PROPERTY_TYPE_MISSING_L);
        }
        if (updatePropertyBean.getType() == PropertyType.FLAT && updatePropertyBean.getPrioset() == null) {
            throw new ApiValidationException(PROPERTY_FLAT_PRIOSET_MISSING_L);
        }
        if (updatePropertyBean.getType() != PropertyType.FLAT && updatePropertyBean.getPrioset() != null) {
            throw new ApiValidationException(PROPERTY_NOT_FLAT_PRIOSET_SHOULD_BE_NULL_L);
        }
        if (updatePropertyBean.getWriteProtection() == null) {
            throw new ApiValidationException(PROPERTY_WRITE_PROTECTION_L);
        }
        if (updatePropertyBean.getConversationConfigs() == null) {
            throw new ApiValidationException(PROPERTY_CONVERSATION_CONFIG_MISSING_L);
        }

        validateAvailableFrom(updatePropertyBean.getData());
    }

    private void validateAvailableFrom(PropertyData data) {
        AvailableFrom availableFrom = data.getAvailableFrom();
        if (availableFrom != null &&
                availableFrom.getStringAvailable() != null &&
                availableFrom.getStringAvailable().length() > AVAILABLE_FROM_STRING_MAX_SIZE) {
            throw new ApiValidationException(AVAILABLE_FROM_STRING_TO_LONG_L);
        }
    }

    private void checkRentStatus(Property property) {
        if (!property.isRented()) {
            log.error("unable to unrent property: " + property.getId() + " " + FLAT_IS_NOT_RENTED);
            throw new ImmomioRuntimeException(FLAT_IS_NOT_RENTED);
        }
    }
}
