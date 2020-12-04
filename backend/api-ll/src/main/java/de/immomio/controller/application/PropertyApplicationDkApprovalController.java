package de.immomio.controller.application;

import de.immomio.constants.exceptions.ApplicationSaveException;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.landlord.service.reporting.indexing.delegate.LandlordApplicationIndexingDelegate;
import de.immomio.service.application.DkApprovalService;
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

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/applications/dk")
public class PropertyApplicationDkApprovalController {

    private final DkApprovalService dkApprovalService;

    private final LandlordApplicationIndexingDelegate applicationIndexingDelegate;

    @Autowired
    public PropertyApplicationDkApprovalController(
            DkApprovalService dkApprovalService,
            LandlordApplicationIndexingDelegate applicationIndexingDelegate
    ) {
        this.dkApprovalService = dkApprovalService;
        this.applicationIndexingDelegate = applicationIndexingDelegate;
    }

    @PreAuthorize("#application.property.customer.id == principal.customer.id")
    @PostMapping(value = "/increase/{application}")
    public ResponseEntity<Object> increase(
           @Parameter(hidden = true)  PersistentEntityResourceAssembler assembler,
            @PathVariable("application") PropertyApplication application
    ) {
        try {
            PropertyApplication saved = dkApprovalService.increase(application);
            applicationIndexingDelegate.dkLevelChanged(saved);
        } catch (ApplicationSaveException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(assembler.toModel(application), HttpStatus.OK);
    }

    @PreAuthorize("#application.property.customer.id == principal.customer.id")
    @PostMapping(value = "/decrease/{application}")
    public ResponseEntity<Object> decrease(
           @Parameter(hidden = true)  PersistentEntityResourceAssembler assembler,
            @PathVariable("application") PropertyApplication application
    ) {
        try {
            PropertyApplication saved = dkApprovalService.decrease(application);
            applicationIndexingDelegate.dkLevelChanged(saved);
        } catch (ApplicationSaveException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(assembler.toModel(application), HttpStatus.OK);
    }

    @PreAuthorize("#application.property.customer.id == principal.customer.id")
    @PostMapping(value = "/reset/{application}")
    public ResponseEntity<Object> reset(
            @Parameter(hidden = true)  PersistentEntityResourceAssembler assembler,
            @PathVariable("application") PropertyApplication application
    ) {
        try {
            PropertyApplication saved = dkApprovalService.reset(application);
            applicationIndexingDelegate.dkLevelChanged(saved);
        } catch (ApplicationSaveException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(assembler.toModel(application), HttpStatus.OK);
    }

}
