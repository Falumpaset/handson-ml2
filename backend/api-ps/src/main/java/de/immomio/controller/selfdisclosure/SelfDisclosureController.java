package de.immomio.controller.selfdisclosure;

import de.immomio.beans.shared.selfdisclosure.SelfDisclosureBean;
import de.immomio.beans.shared.selfdisclosure.SelfDisclosureResponseBean;
import de.immomio.constants.exceptions.NotAuthorizedException;
import de.immomio.constants.exceptions.ResponseForbiddenException;
import de.immomio.constants.exceptions.ResponseValidationException;
import de.immomio.controller.BaseController;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.propertysearcher.entity.user.PropertySearcherUserProfile;
import de.immomio.data.shared.entity.property.propertyApplication.PropertyApplication;
import de.immomio.service.property.PropertyService;
import de.immomio.service.security.UserSecurityService;
import de.immomio.service.selfdisclosure.PropertySearcherSelfDisclosureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
public class SelfDisclosureController extends BaseController {

    private static final String USER_HAS_ALREADY_ANSWERED_SELF_QUESTION = "USER_HAS_ALREADY_ANSWERED_SELF_QUESTION_L";

    private static final String UQ_USER_SELF_DISCLOSURE_QUESTION = "uq_user_self_disclosure_question_response";

    private final PropertyService propertyService;

    private final UserSecurityService userSecurityService;

    private final PropertySearcherSelfDisclosureService propertySearcherSelfDisclosureService;

    @Autowired
    public SelfDisclosureController(
            PropertyService propertyService,
            UserSecurityService userSecurityService,
            PropertySearcherSelfDisclosureService propertySearcherSelfDisclosureService
    ) {
        this.propertyService = propertyService;
        this.userSecurityService = userSecurityService;
        this.propertySearcherSelfDisclosureService = propertySearcherSelfDisclosureService;
    }

    @GetMapping("/{propertyId}")
    public ResponseEntity<?> findByProperty(@PathVariable("propertyId") Long propertyId) {
        try {
            Property property = propertyService.findById(propertyId);
            LandlordCustomer customer = property.getCustomer();

            if (!propertySearcherSelfDisclosureService.allowSelfDisclosureProcess(property)) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }

            SelfDisclosureBean selfDisclosureBean = propertySearcherSelfDisclosureService.mergeInSelfDisclosureBean(customer.getSelfDisclosure());

            return new ResponseEntity<>(selfDisclosureBean, HttpStatus.OK);
        } catch (NotAuthorizedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PreAuthorize("#application.userProfile.user.id == principal.id")
    @GetMapping(value = "/response/findByApplication/{application}")
    public ResponseEntity<SelfDisclosureResponseBean> findResponse(@PathVariable("application") PropertyApplication application) {
        SelfDisclosureResponseBean response = propertySearcherSelfDisclosureService.findResponse(
                application.getProperty(),
                application.getUserProfile()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/response/{propertyId}")
    public ResponseEntity<Object> createResponse(
            @PathVariable("propertyId") Long propertyId,
            @RequestBody @Valid SelfDisclosureResponseBean responseBean
    ) {
        try {
            PropertySearcherUserProfile userProfile = userSecurityService.getPrincipalUserProfile();
            Property property = propertyService.findById(propertyId);

            propertySearcherSelfDisclosureService.saveOrUpdateResponse(
                    responseBean,
                    property,
                    userProfile
            );

            return ResponseEntity.accepted().build();
        } catch (ResponseValidationException | ResponseForbiddenException e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DataIntegrityViolationException ex) {
            log.error(ex.getMessage(), ex);

            if (ex.getMessage().contains(UQ_USER_SELF_DISCLOSURE_QUESTION)) {
                return ResponseEntity.badRequest().body(USER_HAS_ALREADY_ANSWERED_SELF_QUESTION);
            } else {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (NotAuthorizedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

}
