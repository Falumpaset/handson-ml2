package de.immomio.controller.appointment;

import com.google.common.io.Files;
import de.immomio.beans.landlord.InviteToViewingsBean;
import de.immomio.beans.landlord.appointment.AppointmentSearchBean;
import de.immomio.beans.shared.AppointmentCountBean;
import de.immomio.beans.shared.PageAppointmentCountBean;
import de.immomio.common.file.FileUtilities;
import de.immomio.controller.BaseController;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.AppointmentBean;
import de.immomio.data.shared.entity.appointment.AppointmentState;
import de.immomio.landlord.service.appointment.AppointmentNotificationService;
import de.immomio.landlord.service.appointment.AppointmentService;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.model.repository.shared.appointment.AppointmentRepository;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Maik Kingma
 */

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/appointments")
public class AppointmentController extends BaseController {

    private static final String DATE_FORMAT = "yyyy/MM/dd";
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-HH-mm");
    private static final String UNAUTHORIZED_ACCESS = "UNAUTHORIZED_ACCESS_L";
    private static final String ID_PARAM = "id";
    private static final String PARTICIPANT_LIST_NOT_CREATED_L = "PARTICIPANT_LIST_NOT_CREATED_L";
    private static final String PARTICIPANT_FILENAME_CSV = "participants-list-%s.csv";
    private static final String CSV_CONTENT_TYPE = "text/csv; charset=ISO-8859-1";

    private final AppointmentRepository appointmentRepository;

    private final UserSecurityService userSecurityService;

    private final AppointmentService appointmentService;

    private final AppointmentNotificationService appointmentNotificationService;

    private final  PagedResourcesAssembler<Appointment> pagedResourcesAssembler;

    @Autowired
    public AppointmentController(AppointmentRepository appointmentRepository,
            UserSecurityService userSecurityService,
            AppointmentService appointmentService,
            AppointmentNotificationService appointmentNotificationService, PagedResourcesAssembler<Appointment> pagedResourcesAssembler) {
        this.appointmentRepository = appointmentRepository;
        this.userSecurityService = userSecurityService;
        this.appointmentService = appointmentService;
        this.appointmentNotificationService = appointmentNotificationService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @PostMapping(value = "/cancel")
    public ResponseEntity<Object> cancelAppointment(
            @RequestParam(ID_PARAM) Long id
    ) {
        Appointment appointment = appointmentRepository.customFindOne(id);

        if (checkForAuthorizedAppointmentAccess(appointment)) {
            return new ResponseEntity<>(UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
        }

        try {
            appointmentService.cancelAppointment(appointment);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        AppointmentCountBean appointmentCountBean =
                appointmentService.getSingleAppointmentCountBean(AppointmentState.ACTIVE, appointment);
        return new ResponseEntity<>(appointmentCountBean, HttpStatus.OK);
    }

    @PostMapping("/{appointment}/invite-to-viewings")
    @PreAuthorize("#appointment.property.customer.id == principal.customer.id")
    public ResponseEntity<?> inviteToViewings(
            @PathVariable("appointment") Appointment appointment,
            @RequestBody InviteToViewingsBean invite
    ) {
        appointmentService.inviteToViewings(appointment, invite.getEmails());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PreAuthorize("#appointment.property.customer.id == principal.customer.id")
    @ResponseBody
    @GetMapping("/downloadParticipants")
    public ResponseEntity<?> downloadParticipants(
            @RequestParam("appointment") Appointment appointment,
            HttpServletResponse httpResponse
    ) throws IOException {
        File tempDirectory = Files.createTempDir();
        File csvContent = appointmentService.generateAppointmentCsvData(appointment, tempDirectory);
        String fileName = String.format(PARTICIPANT_FILENAME_CSV, TIME_FORMATTER.format(LocalDateTime.now()));

        ResponseEntity<Object> response = createFileToResponse(
                FileUtils.readFileToByteArray(csvContent),
                httpResponse,
                fileName,
                PARTICIPANT_LIST_NOT_CREATED_L,
                CSV_CONTENT_TYPE,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        FileUtilities.forceDelete(tempDirectory);

        return response;
    }

    @PreAuthorize("#appointment.property.customer.id == principal.customer.id")
    @PostMapping("/sendParticipantsAsEmail")
    public ResponseEntity<?> sendParticipantListAsEmail(
            @RequestParam("email") String email,
            @RequestParam("appointment") Appointment appointment
    ) {
        appointmentService.sendParticipantListAsEmail(email, appointment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("#appointment.property.customer.id == principal.customer.id")
    @PostMapping("/update/{appointment}")
    public ResponseEntity edit(
            @PathVariable("appointment") Appointment appointment,
            @RequestBody AppointmentBean appointmentBean,
           @Parameter(hidden = true)  PersistentEntityResourceAssembler assembler
    ) {
        Appointment changedAppointment = appointmentService.editAppointment(appointment, appointmentBean);

        return ResponseEntity.ok(assembler.toModel(changedAppointment));
    }

    @PostMapping("/create")
    public ResponseEntity create(
            @RequestBody List<AppointmentBean> appointments
    ) {
        List<Appointment> createdAppointments = appointmentService.createMultipleAppointments(appointments);

        return ResponseEntity.ok(createdAppointments);
    }

    @PostMapping("/search/list")
    public ResponseEntity searchAppointments(@RequestBody AppointmentSearchBean searchBean) {
        PageRequest pageRequest = PageRequest.of(searchBean.getPage(), searchBean.getSize(), searchBean.getSort());
        Page<Appointment> appointments = appointmentService.search(searchBean, pageRequest);
        PagedModel<EntityModel<Appointment>> appointmentModel = pagedResourcesAssembler.toModel(appointments);
        PageAppointmentCountBean pageAppointmentCountBean = appointmentService
                .getPageAppointmentCountBean(appointmentModel, searchBean);

        return ResponseEntity.ok(pageAppointmentCountBean);
    }

    private boolean checkForAuthorizedAppointmentAccess(Appointment appointment) {
        LandlordUser user = userSecurityService.getPrincipalUser();
        return !appointment.getProperty().getCustomer().getId().equals(user.getCustomer().getId());
    }
}
