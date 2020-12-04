package de.immomio.landlord.service.application;

import de.immomio.beans.IdBean;
import de.immomio.beans.landlord.application.ApplicationSearchBean;
import de.immomio.beans.shared.CommonCountBean;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.data.landlord.bean.property.dk.DkApprovalLevel;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.application.LandlordPropertyApplicationBean;
import de.immomio.data.shared.bean.note.NoteBean;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.LimitedAppointmentAcceptance;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.data.shared.entity.property.search.PropertySearcherSearchBean;
import de.immomio.data.shared.entity.property.search.PropertySearcherSearchItem;
import de.immomio.landlord.service.note.NoteService;
import de.immomio.landlord.service.propertyuserprofile.PropertyUserProfileAssignmentSearchService;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.repository.landlord.customer.property.dk.DkApprovalRepository;
import de.immomio.model.repository.landlord.propertysearcher.searchprofile.LandlordPropertySearcherUserSearchProfileRepository;
import de.immomio.model.repository.shared.application.PropertyApplicationRepository;
import de.immomio.model.repository.shared.appointment.acceptance.AppointmentAcceptanceRepository;
import de.immomio.model.repository.shared.appointment.invitation.AppointmentInvitationRepository;
import de.immomio.model.repository.shared.conversation.ConversationRepository;
import de.immomio.model.repository.shared.property.PropertyRepository;
import de.immomio.service.application.PropertyApplicationConverter;
import de.immomio.utils.criteria.CriteriaQueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static de.immomio.utils.criteria.CriteriaQueryUtils.addEnumListToPredicates;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@Service
public class LandlordPropertyApplicationSearchService extends PropertyUserProfileAssignmentSearchService<PropertyApplication> {

    public static final double NOT_IN_FAVOUR_THRESHOLD = 2.0;
    public static final double IN_FAVOUR_THRESHOLD = 8.01;
    public static final double PARTLY_IN_FAVOUR_MIN = 2.01;
    public static final double PARTLY_IN_FAVOUR_MAX = 8.0;
    public static final String SCORE_EXCLUDING_RANGE = "scoreExcludingRange";
    private static final String USER_PROFILE = "userProfile";
    private static final String WBS = "additionalInformation.wbs";
    private static final String DATA = "data";
    private static final String SEEN = "seen";
    private static final String PROPERTY = "property";
    private static final String STATUS = "status";
    private static final String CUSTOM_QUESTION_SCORE = "customQuestionScore";

    private static final String NO_APPLICATION_FOUND_FOR_USER_PROPERTY_COMBINATION = "NO_APPLICATION_FOUND_FOR_USER_PROPERTY_COMBINATION_L";
    private final PropertyApplicationRepository applicationRepository;
    private final PropertyRepository propertyRepository;
    private final EntityManager entityManager;
    private final UserSecurityService userSecurityService;
    private final PropertyApplicationConverter applicationConverter;

    private final AppointmentAcceptanceRepository appointmentAcceptanceRepository;
    private final AppointmentInvitationRepository appointmentInvitationRepository;
    private final ConversationRepository conversationRepository;
    private final NoteService noteService;
    private final DkApprovalRepository dkApprovalRepository;
    private LandlordPropertySearcherUserSearchProfileRepository userSearchProfileRepository;

    @Autowired
    public LandlordPropertyApplicationSearchService(PropertyApplicationRepository applicationRepository,
            PropertyRepository propertyRepository,
            EntityManager entityManager,
            UserSecurityService userSecurityService,
            PropertyApplicationConverter applicationConverter,
            AppointmentAcceptanceRepository appointmentAcceptanceRepository,
            AppointmentInvitationRepository appointmentInvitationRepository,
            ConversationRepository conversationRepository,
            NoteService noteService,
            DkApprovalRepository dkApprovalRepository,
            LandlordPropertySearcherUserSearchProfileRepository userSearchProfileRepository) {
        super(userSecurityService);
        this.applicationRepository = applicationRepository;
        this.propertyRepository = propertyRepository;
        this.entityManager = entityManager;
        this.userSecurityService = userSecurityService;
        this.applicationConverter = applicationConverter;
        this.appointmentAcceptanceRepository = appointmentAcceptanceRepository;
        this.appointmentInvitationRepository = appointmentInvitationRepository;
        this.conversationRepository = conversationRepository;
        this.noteService = noteService;
        this.dkApprovalRepository = dkApprovalRepository;
        this.userSearchProfileRepository = userSearchProfileRepository;
    }

    public Page<LandlordPropertyApplicationBean> search(ApplicationSearchBean searchBean, PageRequest pageRequest) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<PropertyApplication> query = builder.createQuery(PropertyApplication.class);
        Root<PropertyApplication> root = query.from(PropertyApplication.class);

        List<Order> orders = new ArrayList<>();
        pageRequest.getSort().iterator().forEachRemaining(sortOrder -> {
            addOrder(builder, root, orders, sortOrder);
        });

        Predicate predicate = createPredicate(searchBean, builder, root);
        Long count = getCount(builder, searchBean);
        int offset = Long.valueOf(pageRequest.getOffset()).intValue();
        List<PropertyApplication> applications = entityManager.createQuery(query.where(predicate).orderBy(orders))
                .setFirstResult(offset)
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();

        if (searchBean.getPropertyId() != null) {
            setPropertyApplicationsViewed(searchBean.getPropertyId());
        }
        LandlordCustomer customer = userSecurityService.getPrincipalUser().getCustomer();
        List<LandlordPropertyApplicationBean> applicationOverviewBeans = applications.parallelStream()
                .map((PropertyApplication application) -> collectApplicationBean(application, customer))
                .collect(Collectors.toList());

        return new PageImpl<>(applicationOverviewBeans, pageRequest, count);
    }

    public List<PropertySearcherSearchBean> findByNameAndEmail(String searchValue) {
        searchValue = searchValue.replace(" ", "%");
        List<PropertyApplication> applications = applicationRepository.findByPsNameOrEmail(searchValue);

        return applications.stream().collect(Collectors.groupingBy(PropertyApplication::getUserProfile)).entrySet().stream().map(entry -> {
            PropertySearcherUserProfile userProfile = entry.getKey();
            List<PropertyApplication> groupedApplications = entry.getValue();
            List<PropertySearcherSearchItem> applicationBeans = groupedApplications.stream()
                    .map(application -> new PropertySearcherSearchItem(application.getProperty(), application.getStatus().name(), application.getScore(),
                            application.getId()))
                    .collect(Collectors.toList());
            return new PropertySearcherSearchBean(userProfile, applicationBeans);
        }).collect(Collectors.toList());
    }

    public CommonCountBean getCountOfApplications(ApplicationSearchBean searchBean) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        return CommonCountBean.builder().count(getCount(builder, searchBean)).build();
    }

    public LandlordPropertyApplicationBean findByUserProfileIdAndPropertyId(Long propertyId, Long userId) {
        PropertyApplication application = applicationRepository.findByUserProfileIdAndPropertyId(userId, propertyId);
        if (application == null) {
            throw new ApiValidationException(NO_APPLICATION_FOUND_FOR_USER_PROPERTY_COMBINATION);
        }
        return collectApplicationBean(application, userSecurityService.getPrincipalUser().getCustomer());
    }

    public LandlordPropertyApplicationBean findByPropertyApplication(AppointmentAcceptance appointmentAcceptance) {
        PropertyApplication application = appointmentAcceptance.getApplication();
        LandlordCustomer customer = userSecurityService.getPrincipalUser().getCustomer();
        if (!customer.equals(application.getProperty().getCustomer())) {
             throw new ApiValidationException("ACCESS_NOT_ALLOWED_L");
        }
        return collectApplicationBean(application, customer);
    }

    public IdBean exists(PropertySearcherUserProfile userProfile, Long propertyId) {
        return applicationRepository.getByUserProfileAndProperty(userProfile, propertyId)
                .map(propertyApplication -> new IdBean(propertyApplication.getId()))
                .orElse(new IdBean());
    }

    private Predicate createPredicate(ApplicationSearchBean searchBean, CriteriaBuilder builder, Root<PropertyApplication> root) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(builder.equal(root.get(PROPERTY), searchBean.getPropertyId()));

        addEnumListToPredicates(searchBean.getStatuses(), builder, root.get(STATUS), predicates);

        if (searchBean.getProcessed() != null) {
            if (searchBean.getProcessed()) {
                predicates.add(builder.isNotNull(root.get(SEEN)));
            } else {
                predicates.add(builder.isNull(root.get(SEEN)));
            }
        }

        if (searchBean.getWbs() != null) {
            CriteriaQueryUtils.populateJsonbEqualPredicate(builder, root.join(USER_PROFILE), DATA, WBS, searchBean.getWbs().toString(), predicates);
        }

        if (searchBean.getCustomQuestionFilter() != null) {
            switch (searchBean.getCustomQuestionFilter()) {
                case CQ_UNANSWERED:
                    CriteriaQueryUtils.populateJsonbIsNullPredicate(builder, root, CUSTOM_QUESTION_SCORE, SCORE_EXCLUDING_RANGE, predicates);
                    break;

                case NOT_IN_FAVOUR:
                    CriteriaQueryUtils.populateJsonbBetweenPredicate(builder, root, CUSTOM_QUESTION_SCORE, SCORE_EXCLUDING_RANGE, BigDecimal.ZERO.doubleValue(),
                            NOT_IN_FAVOUR_THRESHOLD, predicates, Double.class);
                    break;

                case IN_FAVOUR:
                    CriteriaQueryUtils.populateJsonbBetweenPredicate(builder, root, CUSTOM_QUESTION_SCORE, SCORE_EXCLUDING_RANGE, IN_FAVOUR_THRESHOLD,
                            BigDecimal.TEN.doubleValue(), predicates, Double.class);
                    break;

                case PARTLY_IN_FAVOUR:
                    CriteriaQueryUtils.populateJsonbBetweenPredicate(builder, root, CUSTOM_QUESTION_SCORE, SCORE_EXCLUDING_RANGE, PARTLY_IN_FAVOUR_MIN,
                            PARTLY_IN_FAVOUR_MAX, predicates, Double.class);
                    break;
            }
        }
        if (!CollectionUtils.isEmpty(searchBean.getProfileTypes())) {
            Join<PropertyApplication, PropertySearcherUserProfile> userJoin = root.join(USER_PROFILE);
            addEnumListToPredicates(searchBean.getProfileTypes(), builder, userJoin.get("type"), predicates);
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }

    private Long getCount(CriteriaBuilder builder, ApplicationSearchBean applicationSearchBean) {
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<PropertyApplication> root = query.from(PropertyApplication.class);

        Predicate predicate = createPredicate(applicationSearchBean, builder, root);
        query.select(builder.countDistinct(root));

        return entityManager.createQuery(query.where(predicate)).getSingleResult();
    }

    private void setPropertyApplicationsViewed(Long propertyId) {
        Optional<Property> propertyOpt = propertyRepository.findById(propertyId);
        propertyOpt.ifPresent(property -> {
            property.setApplicationsViewed(new Date());
            propertyRepository.save(property);
        });
    }

    private LandlordPropertyApplicationBean collectApplicationBean(PropertyApplication application, LandlordCustomer customer) {
        List<LimitedAppointmentAcceptance> acceptances = appointmentAcceptanceRepository.findLimitedByApplication(application);
        boolean conversationExists = conversationRepository.existsById(application.getId());
        Long invitationCount = appointmentInvitationRepository.countByApplication(application);
        NoteBean noteBean = noteService.findByPSUserProfileIdAndCustomer(application.getUserProfile().getId(), customer);
        List<DkApprovalLevel> dkApprovals = dkApprovalRepository.findLevelByApplication(application,
                PageRequest.of(0, 1, Sort.by(Sort.Direction.DESC, "created")));
        DkApprovalLevel newestDkLevel = dkApprovals.stream().findFirst().orElse(DkApprovalLevel.DK1);
        Date firstSpCreated = userSearchProfileRepository.getFirstCreatedDate(application.getUserProfile());
        return applicationConverter.convertToLandlordPropertyApplicationBean(application, newestDkLevel, acceptances, conversationExists, invitationCount,
                noteBean, firstSpCreated);
    }

}
