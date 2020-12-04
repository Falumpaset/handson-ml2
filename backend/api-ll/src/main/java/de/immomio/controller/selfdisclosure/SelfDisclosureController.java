package de.immomio.controller.selfdisclosure;

import de.immomio.beans.shared.selfdisclosure.SelfDisclosureBean;
import de.immomio.beans.shared.selfdisclosure.SelfDisclosureResponseBean;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.selfdisclosure.SelfDisclosure;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.landlord.service.selfdisclosure.LandlordSelfDisclosureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/selfDisclosure")
public class SelfDisclosureController {

    private final LandlordSelfDisclosureService landlordSelfDisclosureService;

    private final UserSecurityService userSecurityService;

    @Autowired
    public SelfDisclosureController(
            LandlordSelfDisclosureService landlordSelfDisclosureService,
            UserSecurityService userSecurityService
    ) {
        this.landlordSelfDisclosureService = landlordSelfDisclosureService;
        this.userSecurityService = userSecurityService;
    }

    @GetMapping("")
    public ResponseEntity<?> get() {
        LandlordCustomer customer = userSecurityService.getPrincipalUser().getCustomer();

        if (customer.isSelfDisclosureAllowed()) {
            return new ResponseEntity<>(landlordSelfDisclosureService.mergeInSelfDisclosureBean(customer.getSelfDisclosure()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping(value = "")
    public ResponseEntity<Object> save(@RequestBody @Valid SelfDisclosureBean selfDisclosureBean) {
        LandlordCustomer customer = userSecurityService.getPrincipalUser().getCustomer();

        if (customer.isSelfDisclosureAllowed()) {
            SelfDisclosure selfDisclosure = landlordSelfDisclosureService.mergeAndSave(selfDisclosureBean, customer.getSelfDisclosure());

            return new ResponseEntity<>(landlordSelfDisclosureService.mergeInSelfDisclosureBean(selfDisclosure), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PreAuthorize("#application.property.customer.id == principal.customer.id")
    @GetMapping(value = "/response/findByApplication/{application}")
    public ResponseEntity<SelfDisclosureResponseBean> findResponse(@PathVariable("application") PropertyApplication application) {
        SelfDisclosureResponseBean response = landlordSelfDisclosureService.findResponse(
                application.getProperty(),
                application.getUserProfile()
        );

        return ResponseEntity.ok(response);
    }
}
