package de.immomio.landlord.service.appointment;

import com.google.common.io.Files;
import com.opencsv.CSVWriter;
import de.immomio.beans.landlord.appointment.AppointmentSearchBean;
import de.immomio.beans.shared.AppointmentCountBean;
import de.immomio.beans.shared.PageAppointmentCountBean;
import de.immomio.common.file.FileUtilities;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.ImmomioRuntimeException;
import de.immomio.constants.exceptions.NotAuthorizedException;
import de.immomio.data.landlord.bean.property.PropertyMailBean;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.bean.user.AgentInfo;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.landlord.entity.user.LandlordUsertype;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileData;
import de.immomio.data.propertysearcher.bean.user.profile.PropertySearcherUserProfileEmailBean;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.bean.common.Address;
import de.immomio.data.shared.bean.common.DateRange;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.AppointmentBean;
import de.immomio.data.shared.entity.appointment.AppointmentState;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceState;
import de.immomio.data.shared.entity.appointment.attendance.invitation.AppointmentInvitation;
import de.immomio.landlord.service.appointment.invitation.AppointmentInvitationService;
import de.immomio.landlord.service.property.PropertyCountService;
import de.immomio.landlord.service.property.PropertyService;
import de.immomio.landlord.service.reporting.indexing.delegate.LandlordAppointmentIndexingDelegate;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.mail.ModelParams;
import de.immomio.mail.sender.LandlordMailSender;
import de.immomio.mail.sender.templates.MailTemplate;
import de.immomio.model.repository.shared.appointment.AppointmentRepository;
import de.immomio.service.shared.EmailModelProvider;
import de.immomio.utils.criteria.CriteriaQueryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.stream.Collectors;


@Slf4j
@Service
public class AppointmentService {

    private static final String INVITE_TO_VIEWING_SUBJECT = "property.invite.to.viewing.subject";
    private static final String VIEWING_CHANGED_SUBJECT = "property.viewing.changed.subject";
    private static final String SEPARATOR = ";";
    private static final String SPACE = " ";
    private static final String COMMA = ",";
    private static final String FLAT_ID = "Wohnung ID: ";
    private static final String PARTICIPANTS_LIST_FILE_NAME = "Teilnehmerliste.csv";
    private static final String SUBJECT = "appointment.participants.subject";
    private static final String ADDRESS = "Adresse: ";
    private static final String DATE_LABEL = "Datum: ";
    private static final String PARTICIPANT = "Teilnehmer:";
    private static final String PRESENT = "Anwesend:";
    private static final String PHONE = "Phone:";
    private static final String EMAIL = "Email:";
    private static final String REMAKRS = "Anmerkung:";
    private static final String PROPERTY = "property";
    private static final String USER = "user";
    private static final String PROPERTYMANAGER = "propertyManager";
    private static final String ID = "id";
    private static final String STATE = "state";
    private static final String DATE = "date";
    private static final String CUSTOMER = "customer";
    private static final String ADDRESS_FORMAT = "%s %s, %s, %s";
    public static final String APPOINTMENT_ACCESS_NOT_ALLOWED_L = "APPOINTMENT_ACCESS_NOT_ALLOWED_L";

    @Value("${timezone.europe}")
    private String berlinTimezone;

    @Value("${appointment.durationInMinutes}")
    private int durationInMinutes;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    private final LandlordMailSender landlordMailSender;

    private final PropertyCountService populateCountService;

    private final AppointmentRepository appointmentRepository;

    private final PropertyService propertyService;

    private final AppointmentNotificationService appointmentNotificationService;

    private final EmailModelProvider emailModelProvider;

    private final LandlordAppointmentIndexingDelegate appointmentIndexingDelegate;

    private final EntityManager entityManager;

    private final UserSecurityService userSecurityService;

    private final AppointmentInvitationService invitationService;

    @Autowired
    public AppointmentService(
            LandlordMailSender landlordMailSender,
            PropertyCountService populateCountService,
            AppointmentRepository appointmentRepository,
            PropertyService propertyService,
            AppointmentNotificationService appointmentNotificationService,
            EmailModelProvider emailModelProvider,
            LandlordAppointmentIndexingDelegate appointmentIndexingDelegate,
            EntityManager entityManager,
            UserSecurityService userSecurityService,
            AppointmentInvitationService invitationService
    ) {
        this.landlordMailSender = landlordMailSender;
        this.populateCountService = populateCountService;
        this.appointmentRepository = appointmentRepository;
        this.propertyService = propertyService;
        this.appointmentNotificationService = appointmentNotificationService;
        this.emailModelProvider = emailModelProvider;
        this.appointmentIndexingDelegate = appointmentIndexingDelegate;
        this.entityManager = entityManager;
        this.userSecurityService = userSecurityService;
        this.invitationService = invitationService;
    }

    @PostConstruct
    public void init() {
        dateFormat.setTimeZone(TimeZone.getTimeZone(ZoneId.of(berlinTimezone)));
    }

    public void inviteToViewings(Appointment appointment, List<String> emails) {
        appointmentNotificationService.inviteToViewings(appointment, emails);
    }

    public void resetAppointmentAcceptances(List<Appointment> appointments) {
        appointments.forEach(appointment -> appointment.getAppointmentAcceptances()
                .forEach(appointmentAcceptance -> appointmentAcceptance.setState(AppointmentAcceptanceState.CANCELED)));
    }

    public PageAppointmentCountBean getPageAppointmentCountBean(
            PagedModel<EntityModel<Appointment>> appointments,
            AppointmentSearchBean appointmentSearchBean
    ) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        AppointmentSearchBean searchBeanCountUpcoming = new AppointmentSearchBean(
                appointmentSearchBean.getPropertyId(),
                appointmentSearchBean.getAgents(),
                appointmentSearchBean.getState(),
                new DateRange(new Date(), null));

        Long upcoming = getCount(builder, searchBeanCountUpcoming);

        AppointmentSearchBean searchBeanCountPast = new AppointmentSearchBean(
                appointmentSearchBean.getPropertyId(),
                appointmentSearchBean.getAgents(),
                appointmentSearchBean.getState(),
                new DateRange(null, new Date()));


        Long past = getCount(builder, searchBeanCountPast);

        return new PageAppointmentCountBean(appointments, past, upcoming);
    }

    public AppointmentCountBean getSingleAppointmentCountBean(AppointmentState state, Appointment appointment) {
        Long upcoming = appointmentRepository.countByStateAndDateAfter(state, new Date());
        Long past = appointmentRepository.countByStateAndDateBefore(state, new Date());

        return new AppointmentCountBean(appointment, past, upcoming);
    }

    public Appointment editAppointment(Appointment appointment, AppointmentBean appointmentBean) {
        validateAccessForPropertyManager(appointment.getProperty());
        boolean sendMail = false;

        if (valuesDiffer(appointmentBean.getContact(), appointment.getContact())) {
            sendMail = true;
            appointment.setContact(appointmentBean.getContact());
        }
        if (valuesDiffer(appointmentBean.getDate() != null, appointment.getDate())) {
            sendMail = true;
            appointment.setDate(appointmentBean.getDate());
        }
        if (valuesDiffer(appointmentBean.getShowContactInformation(), appointment.getShowContactInformation())) {
            sendMail = true;
            appointment.setShowContactInformation(appointmentBean.getShowContactInformation());
        }
        if (valuesDiffer(appointmentBean.getMaxInviteeCount(), appointment.getMaxInviteeCount())) {
            appointment.setMaxInviteeCount(appointmentBean.getMaxInviteeCount());
        }
        if (valuesDiffer(appointmentBean.getSpecialInstructions(), appointment.getSpecialInstructions())) {
            sendMail = true;
            appointment.setSpecialInstructions(appointmentBean.getSpecialInstructions());
        }

        Appointment savedAppointment = appointmentRepository.save(appointment);

        appointmentIndexingDelegate.appointmentUpdated(appointment);
        if (sendMail) {
            appointmentNotificationService.appointmentChanged(appointment);
        }

        return savedAppointment;
    }

    public void cancelAppointment(Appointment appointment) {
        validateAccessForPropertyManager(appointment.getProperty());
        List<AppointmentAcceptance> activeAcceptances = appointment.getAppointmentAcceptances()
                .stream()
                .filter(acceptance -> acceptance.getState() == AppointmentAcceptanceState.ACTIVE)
                .collect(Collectors.toList());

        appointment.getAppointmentAcceptances()
                .forEach(acceptance -> acceptance.setState(AppointmentAcceptanceState.CANCELED));

        appointment.setState(AppointmentState.CANCELED);
        appointmentRepository.customSave(appointment);
        appointmentNotificationService.appointmentCanceled(activeAcceptances);
        appointmentIndexingDelegate.appointmentCanceled(appointment);
    }

    @SuppressWarnings("checkstyle:WhitespaceAround")
    public File generateAppointmentCsvData(Appointment appointment, File directory) {
        validateAccessForPropertyManager(appointment.getProperty());
        String path = directory.getAbsolutePath() + File.separator + PARTICIPANTS_LIST_FILE_NAME;
        File file = new File(path);

        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             BufferedWriter bufferedWriter =
                     new BufferedWriter(new OutputStreamWriter(fileOutputStream, StandardCharsets.ISO_8859_1));
             CSVWriter writer = getCsvWriter(bufferedWriter)) {

            String viewingDate = dateFormat.format(appointment.getDate());
            PropertyData propertyData = appointment.getProperty().getData();

            var property = appointment.getProperty();
            List<PropertySearcherUserProfile> users = appointment.getAppointmentAcceptances().stream()
                    .filter(appointmentAcceptance -> appointmentAcceptance.getState() == AppointmentAcceptanceState.ACTIVE)
                    .map(appointmentAcceptance -> appointmentAcceptance.getApplication().getUserProfile())
                    .collect(Collectors.toList());

            String address = getAddressString(propertyData.getAddress());

            writer.writeNext(new String[]{propertyData.getName()});
            writer.writeNext(new String[]{ADDRESS + address});


            if (property.getExternalId() != null) {
                writer.writeNext(new String[]{FLAT_ID, property.getExternalId()});
            }

            writer.writeNext(new String[]{DATE_LABEL + viewingDate});
            writer.writeNext(new String[]{});
            writer.writeNext(new String[]{PARTICIPANT, PHONE, EMAIL, PRESENT, REMAKRS});

            users.stream().map(this::getUserInfo).forEach(writer::writeNext);
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return file;
    }

    public void sendParticipantListAsEmail(String email, Appointment appointment) {
        validateAccessForPropertyManager(appointment.getProperty());
        Map<String, String> attachments = new HashMap<>();
        Map<String, Object> data = new HashMap<>();

        List<PropertySearcherUserProfileEmailBean> userBeans = appointment.getAppointmentAcceptances().stream()
                .filter(appointmentAcceptance -> appointmentAcceptance.getState() == AppointmentAcceptanceState.ACTIVE)
                .map(appointmentAcceptance -> new PropertySearcherUserProfileEmailBean(appointmentAcceptance.getApplication().getUserProfile()))
                .collect(Collectors.toList());

        data.put(ModelParams.MODEL_USERS, userBeans);
        data.put(ModelParams.MODEL_FLAT, new PropertyMailBean(appointment.getProperty()));

        File tempDirectory = Files.createTempDir();
        File fileToSend = generateAppointmentCsvData(appointment, tempDirectory);
        attachments.put(PARTICIPANTS_LIST_FILE_NAME, fileToSend.getAbsolutePath());
        emailModelProvider.appendFormattedDate(appointment.getDate(), data, ModelParams.MODEL_APPOINTMENT_DATE_FORMATTED);

        landlordMailSender.send(email, MailTemplate.APPOINTMENT_PARTICIPANTS, SUBJECT, data, attachments,
                true);

        FileUtilities.forceDelete(tempDirectory);
    }

    public List<Appointment> createMultipleAppointments(List<AppointmentBean> appointments) {
        List<Appointment> savedAppointments = appointments.stream()
                .map(appointmentBean -> {
                    if (appointmentBean.getApplicationId() == null) {
                        return saveAppointmentBean(appointmentBean);
                    } else {
                        return saveExclusiveAppointment(appointmentBean);
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        savedAppointments.stream()
                .filter(appointment -> !appointment.isExclusive())
                .findFirst()
                .ifPresent(appointmentNotificationService::publicAppointmentCreated);

        savedAppointments.stream()
                .filter(Appointment::isExclusive)
                .findFirst()
                .ifPresent(appointmentNotificationService::exclusiveAppointmentCreated);

        savedAppointments.forEach(appointmentIndexingDelegate::appointmentCreated);

        return savedAppointments;
    }

    private Appointment saveExclusiveAppointment(AppointmentBean appointmentBean) {
        Appointment savedAppointment = saveAppointmentBean(appointmentBean);
        saveAppointmentInvitation(appointmentBean, savedAppointment);
        savedAppointment.setExclusive(true);
        return savedAppointment;
    }

    private AppointmentInvitation saveAppointmentInvitation(AppointmentBean appointmentBean, Appointment savedAppointment) {
        try {
            return invitationService.create(savedAppointment, appointmentBean.getApplicationId());
        } catch (Exception e) {
            //if the invitation couldnt be saved, the appointment needs to be deleted
            appointmentRepository.delete(savedAppointment);
            throw new ImmomioRuntimeException(e.getMessage());
        }
    }

    public Page<Appointment> search(AppointmentSearchBean searchBean, PageRequest pageRequest) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Appointment> query = builder.createQuery(Appointment.class);
        Root<Appointment> root = query.from(Appointment.class);
        List<Order> orders = new ArrayList<>();
        pageRequest.getSort()
                .iterator()
                .forEachRemaining(sortOrder -> CriteriaQueryUtils.addOrder(root, orders, sortOrder));

        Long count = getCount(builder, searchBean);
        Predicate predicate = createPredicate(searchBean, builder, root);
        int offset = Long.valueOf(pageRequest.getOffset()).intValue();
        List<Appointment> appointments = entityManager.createQuery(query.where(predicate)
                .orderBy(orders))
                .setFirstResult(offset)
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();

        return new PageImpl<>(appointments, pageRequest, count);
    }

    private Predicate createPredicate(
            AppointmentSearchBean searchBean,
            CriteriaBuilder builder,
            Root root
    ) {
        List<Predicate> predicates = new ArrayList<>();
        if (searchBean.getPropertyId() != null) {
            predicates.add(builder.equal(root.get(PROPERTY), searchBean.getPropertyId()));
        }
        Join propertyJoin = root.join(PROPERTY);
        LandlordUser principalUser = userSecurityService.getPrincipalUser();
        LandlordCustomer customer = principalUser.getCustomer();
        Predicate customerPredicate = builder.equal(propertyJoin.get(CUSTOMER), customer);
        predicates.add(customerPredicate);

        if (principalUser.getUsertype() == LandlordUsertype.PROPERTYMANAGER) {
            predicates.add(builder.equal(propertyJoin.get(PROPERTYMANAGER), principalUser));
        } else if (searchBean.getAgents() != null && searchBean.getAgents().size() > 0) {
            CriteriaBuilder.In<Long> userClause = builder.in(propertyJoin.get(USER));
            searchBean.getAgents().forEach(userClause::value);
            predicates.add(userClause);
        }

        if (searchBean.getState() != null) {
            predicates.add(builder.equal(root.get(STATE), searchBean.getState()));
        }

        DateRange range = searchBean.getRange();
        if (range != null) {
            if (range.getFrom() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get(DATE), range.getFrom()));
            }
            if (range.getTo() != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get(DATE), range.getTo()));
            }
        }

        return builder.and(predicates.toArray(new Predicate[0]));
    }

    private Long getCount(CriteriaBuilder builder, AppointmentSearchBean searchBean) {
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Appointment> root = query.from(Appointment.class);

        Predicate predicate = createPredicate(searchBean, builder, root);
        query.select(builder.countDistinct(root));

        return entityManager.createQuery(query.where(predicate)).getSingleResult();
    }

    private String getAddressString(Address propertyAddress) {
        return String.format(ADDRESS_FORMAT,
                propertyAddress.getStreet(),
                propertyAddress.getHouseNumber(),
                propertyAddress.getZipCode(),
                propertyAddress.getCity());
    }

    private String[] getUserInfo(PropertySearcherUserProfile userProfile) {
        PropertySearcherUserProfileData profileData = userProfile.getData();
        return new String[]{
                profileData.getFirstname() + SPACE + profileData.getName(),
                profileData.getPhone(),
                userProfile.getEmail()};
    }

    private Appointment saveAppointmentBean(AppointmentBean appointmentBean) {
        Property property;
        try {
            property = propertyService.findById(appointmentBean.getPropertyId());
            validateAccessForPropertyManager(property);
            LandlordUser principalUser = userSecurityService.getPrincipalUser();

            Appointment appointment = new Appointment();
            appointment.setSpecialInstructions(appointmentBean.getSpecialInstructions());
            appointment.setMaxInviteeCount(appointmentBean.getMaxInviteeCount());
            appointment.setShowContactInformation(appointmentBean.getShowContactInformation());
            appointment.setDate(appointmentBean.getDate());
            appointment.setContact(appointmentBean.getContact());
            appointment.setState(appointmentBean.getState());
            appointment.setProperty(property);
            appointment.setAgentInfo(new AgentInfo(principalUser));

            return appointmentRepository.save(appointment);
        } catch (NotAuthorizedException e) {
            log.error("not allowed to access property " + appointmentBean.getId());
            throw new ImmomioRuntimeException("ACCESS_NOT_ALLOWED_L");
        }
    }

    private CSVWriter getCsvWriter(Writer writer) {
        return new CSVWriter(writer,
                ';',
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);
    }

    private boolean valuesDiffer(Object first, Object second) {
        return first != null && !first.equals(second);
    }

    private void validateAccessForPropertyManager(Property property) {
        LandlordUser principalUser = userSecurityService.getPrincipalUser();
        if (principalUser.getUsertype() == LandlordUsertype.PROPERTYMANAGER && !principalUser.equals(property.getPropertyManager())) {
            throw new ApiValidationException(APPOINTMENT_ACCESS_NOT_ALLOWED_L);
        }
    }
}
