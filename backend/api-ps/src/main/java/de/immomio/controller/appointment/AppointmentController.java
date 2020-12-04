package de.immomio.controller.appointment;

import de.immomio.controller.BaseController;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.model.repository.shared.appointment.acceptance.AppointmentAcceptanceRepository;
import de.immomio.propertysearcher.bean.AppointmentBundleBean;
import de.immomio.service.application.PropertyApplicationNotificationService;
import de.immomio.service.appointment.AppointmentService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author Maik Kingma
 */
@Slf4j
@RepositoryRestController
@RequestMapping(value = "/appointments")
public class AppointmentController extends BaseController {


    private static final String ERROR_AT_CREATING_ICS_FILE = "ERROR_AT_CREATING_ICS_FILE_L";
    private static final String APPOINTMENT_FILE_NAME = "besichtigungstermin.ics";
    private static final String ICS_CONTENT_TYPE = "ics";

    private final AppointmentAcceptanceRepository appointmentAcceptanceRepository;

    private final PropertyApplicationNotificationService propertyApplicationNotificationService;

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(
            AppointmentAcceptanceRepository appointmentAcceptanceRepository,
            PropertyApplicationNotificationService propertyApplicationNotificationService,
            AppointmentService appointmentService
    ) {
        this.appointmentAcceptanceRepository = appointmentAcceptanceRepository;
        this.propertyApplicationNotificationService = propertyApplicationNotificationService;
        this.appointmentService = appointmentService;
    }

    @PostMapping(value = "/accept")
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "appointment", schema = @Schema(type = "Long"), required = true),
            @Parameter(in = ParameterIn.QUERY, name = "application", schema = @Schema(type = "Long"), required = true)})
    @PreAuthorize("#application.userProfile.user.id == principal.id")
    public ResponseEntity<Object> acceptAppointment(@Parameter(hidden = true) PersistentEntityResourceAssembler assembler,
            @RequestParam("appointment") Appointment appointment,
            @RequestParam("application") PropertyApplication application) {
        AppointmentAcceptance saved = appointmentService.acceptViewing(appointment, application);

        return ResponseEntity.ok(assembler.toModel(saved));
    }

    @PreAuthorize("@userSecurityService.maySeeAppointments(#appointment.property, principal?.id)")
    @GetMapping(value = "/getAppointmentIcs")
    public ResponseEntity<?> getAppointmentIcsFile(
            @RequestParam("appointment") Appointment appointment,
            HttpServletResponse response
    ) {
        try {
            File invite = appointmentService.generateAppointmentIcsFile(appointment);
            String icsFileContent = IOUtils.toString(new FileInputStream(invite), Charset.defaultCharset());

            return createFileToResponse(
                    icsFileContent.getBytes(StandardCharsets.ISO_8859_1),
                    response,
                    APPOINTMENT_FILE_NAME,
                    ERROR_AT_CREATING_ICS_FILE,
                    ICS_CONTENT_TYPE,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(ERROR_AT_CREATING_ICS_FILE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("#userProfile.user.id == principal.id")
    @GetMapping(value = "/appointmentOverview/{userProfile}")
    public ResponseEntity<List<AppointmentBundleBean>> getAppointmentOverview(@PathVariable("userProfile") PropertySearcherUserProfile userProfile) {
        return ResponseEntity.ok(appointmentService.getAppointmentBundleBeans(userProfile));
    }
}
