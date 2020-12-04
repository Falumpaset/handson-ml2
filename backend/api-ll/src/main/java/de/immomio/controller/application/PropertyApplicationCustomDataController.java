package de.immomio.controller.application;

import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataBundle;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataEmailRequestBean;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataFieldBaseBean;
import de.immomio.data.landlord.bean.application.customData.ApplicationCustomDataRequestBean;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.landlord.service.application.customData.LandlordPropertyApplicationCustomDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/applications/customData")
public class PropertyApplicationCustomDataController {

    private final LandlordPropertyApplicationCustomDataService applicantService;

    @Autowired
    public PropertyApplicationCustomDataController(LandlordPropertyApplicationCustomDataService applicantService) {
        this.applicantService = applicantService;
    }

    @GetMapping("/fields")
    @PreAuthorize("#property.customer.id == principal.customer.id")
    public ResponseEntity<List<ApplicationCustomDataFieldBaseBean>> getFields(@RequestParam("property") Property property) {
        return ResponseEntity.ok(applicantService.getPossibleFields(property));
    }

    @PostMapping
    public ResponseEntity<ApplicationCustomDataBundle> mapApplicationsToFields(@RequestBody ApplicationCustomDataRequestBean requestBean) {
        return ResponseEntity.ok(applicantService.mapApplicationsToFields(requestBean));
    }

    @PostMapping("/send")
    public ResponseEntity<Void> sendDataFile(@RequestBody ApplicationCustomDataEmailRequestBean requestBean) {
        applicantService.sendDataFile(requestBean);
        return ResponseEntity.ok().build();
    }
}
