package de.immomio.controller.followup;

import de.immomio.beans.IdBean;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.followup.Followup;
import de.immomio.data.landlord.entity.property.followup.bean.FollowupBean;
import de.immomio.landlord.service.followup.FollowupService;
import de.immomio.landlord.service.property.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Niklas Lindemann
 */
@Slf4j
@RepositoryRestController
@RequestMapping(value = "/followups")
public class FollowupController {

    private FollowupService followupService;
    private PropertyService propertyService;

    @Autowired
    public FollowupController(FollowupService followupService, PropertyService propertyService) {
        this.followupService = followupService;
        this.propertyService = propertyService;
    }

    @PostMapping
    @Operation(summary = "Creates a followup")
    @PreAuthorize("#property.customer.id == principal.customer.id")
    public ResponseEntity<IdBean> create(
            @RequestParam("property") Property property,
            @RequestParam(value = "setReserved", required = false, defaultValue = "false") Boolean setReserved,
            @RequestBody FollowupBean followupBean) {
        Followup followup = followupService.create(property, followupBean, setReserved);
        return ResponseEntity.ok(new IdBean(followup.getId()));
    }

    @PatchMapping("/{followup}")
    @Operation(summary = "Updates a followup")
    @PreAuthorize("#followup.property.customer.id == principal.customer.id")
    public ResponseEntity<?> edit(@PathVariable("followup") Followup followup, @RequestBody FollowupBean followupBean) {
        followupService.saveFollowup(followup, followupBean);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{followup}/setProcessed")
    @PreAuthorize("#followup.property.customer.id == principal.customer.id")
    public ResponseEntity<?> edit(@PathVariable("followup") Followup followup, @RequestParam("processed") Boolean processed) {
        followupService.setProcessed(followup, processed);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{followup}")
    @PreAuthorize("#followup.property.customer.id == principal.customer.id")
    public ResponseEntity<?> delete(@PathVariable("followup") Followup followup) {
        followupService.delete(followup);

        return ResponseEntity.ok().build();
    }
}
