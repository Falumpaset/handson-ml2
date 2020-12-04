package de.immomio.controller.application;

import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.landlord.service.application.LandlordPropertyApplicationService;
import de.immomio.mail.sender.templates.MailTemplate;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Maik Kingma
 */

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/applications")
public class PropertyApplicationController {

    private final LandlordPropertyApplicationService propertyApplicationService;

    @Autowired
    public PropertyApplicationController(LandlordPropertyApplicationService propertyApplicationService) {
        this.propertyApplicationService = propertyApplicationService;
    }

    @PostMapping(value = "/accept")
    public ResponseEntity<Object> acceptApplicationList(
            @RequestBody List<Long> applicationIds,
            @RequestParam(value = "template", required = false) MailTemplate mailTemplate
    ) {
        propertyApplicationService.acceptApplicationList(applicationIds, mailTemplate);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/reject")
    public ResponseEntity<Object> rejectApplication(
            @RequestBody List<Long> applicationIds,
            @RequestParam(value = "template", required = false) MailTemplate mailTemplate
    ) {
        propertyApplicationService.rejectApplicationList(applicationIds, mailTemplate);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/unreject")
    public ResponseEntity<Object> unrejectApplicationList(@RequestBody List<Long> applicationIds) {
        propertyApplicationService.unrejectApplicationList(applicationIds);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/pretenantviewing")
    public ResponseEntity<Object> setPreTenantViewing(
            @RequestBody List<Long> applicationIds
    ) {
        propertyApplicationService.setPreTenantViewing(applicationIds);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/askForIntent/{application}")
    @PreAuthorize("#application.property.customer.id == principal.customer.id")
    public ResponseEntity<Object> askForIntent(
            @PathVariable("application") PropertyApplication application
    ) {
        propertyApplicationService.askForIntent(application);

        return ResponseEntity.ok().build();
    }

    @PostMapping("tagSeen")
    public ResponseEntity<Object> processApplication(
            @RequestParam("seen") Boolean seen,
            @RequestBody List<Long> applications
    ) {

        propertyApplicationService.tagSeen(applications, seen);

        return ResponseEntity.ok().build();
    }

}
