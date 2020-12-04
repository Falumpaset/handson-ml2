package de.immomio.controller.appointment;

import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import de.immomio.service.appointment.AppointmentAcceptanceService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Maik Kingma
 */
@Slf4j
@RepositoryRestController
@RequestMapping(value = "/appointmentAcceptances")
public class AppointmentAcceptanceController {

    private final AppointmentAcceptanceService appointmentAcceptanceService;

    @Autowired
    public AppointmentAcceptanceController(AppointmentAcceptanceService appointmentAcceptanceService) {
        this.appointmentAcceptanceService = appointmentAcceptanceService;
    }

    @PreAuthorize("#appointmentAcceptance.application?.userProfile?.user.id == principal?.id")
    @PostMapping(value = "/cancel/{appointmentAcceptance}")
    public ResponseEntity cancelAppointment(
            @Parameter(hidden = true)  PersistentEntityResourceAssembler assembler,
            @PathVariable("appointmentAcceptance") AppointmentAcceptance appointmentAcceptance
    ) {
        AppointmentAcceptance saved = appointmentAcceptanceService.cancel(appointmentAcceptance);
        return ResponseEntity.ok(assembler.toModel(saved));
    }
}
