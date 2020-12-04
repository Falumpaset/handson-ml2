package de.immomio.controller.appointment.acceptance;

import de.immomio.data.shared.entity.appointment.Appointment;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptance;
import de.immomio.data.shared.entity.appointment.attendance.acceptance.AppointmentAcceptanceState;
import de.immomio.landlord.service.appointment.acceptance.AppointmentAcceptanceService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static de.immomio.utils.repository.ResourcePageableUtils.DEFAULT_VALUE_PAGE;
import static de.immomio.utils.repository.ResourcePageableUtils.DEFAULT_VALUE_SIZE;

/**
 * @author Andreas Hansen
 */
@Slf4j
@RepositoryRestController
@RequestMapping(value = "/appointmentAcceptances")
public class AppointmentAcceptanceController {

    private final PagedResourcesAssembler pagedResourcesAssembler;
    private final AppointmentAcceptanceService appointmentAcceptanceService;

    @Autowired
    public AppointmentAcceptanceController(
            AppointmentAcceptanceService appointmentAcceptanceService,
            PagedResourcesAssembler pagedResourcesAssembler
    ) {
        this.appointmentAcceptanceService = appointmentAcceptanceService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @PreAuthorize("#appointment.property.customer.id == principal?.customer.id")
    @GetMapping(value = "/search/findByAppointmentAndState")
    public ResponseEntity findByAppointmentAndState(
            @RequestParam("appointment") Appointment appointment,
            @RequestParam("state") AppointmentAcceptanceState state,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "page", required = false, defaultValue = DEFAULT_VALUE_PAGE) int page,
            @RequestParam(value = "size", required = false, defaultValue = DEFAULT_VALUE_SIZE) int size,
            @Parameter(hidden = true)  PersistentEntityResourceAssembler assembler
    ) {
        Page<AppointmentAcceptance> result = appointmentAcceptanceService
                .findByAppointmentAndState(appointment, state, page, size, sort);

        return ResponseEntity.ok(pagedResourcesAssembler.toModel(result, assembler));
    }
}
