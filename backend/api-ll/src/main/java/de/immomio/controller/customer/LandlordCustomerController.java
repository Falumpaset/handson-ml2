package de.immomio.controller.customer;

import de.immomio.beans.TokenBean;
import de.immomio.controller.BaseController;
import de.immomio.data.landlord.bean.customer.LandlordCustomerBean;
import de.immomio.data.landlord.bean.customer.settings.theme.LandlordCustomerBranding;
import de.immomio.data.landlord.entity.settings.LandlordCustomerSettings;
import de.immomio.data.landlord.entity.user.LandlordUser;
import de.immomio.landlord.service.branding.BrandingService;
import de.immomio.landlord.service.customer.LandlordCustomerService;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.security.common.bean.BrandingUrlTokenData;
import de.immomio.security.common.bean.PreviewBrandingUrlToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/customers")
public class LandlordCustomerController extends BaseController {

    private final static String TOKEN_NOT_VALID = "TOKEN_NOT_VALID_L";

    private final BrandingService brandingService;

    private final UserSecurityService userSecurityService;

    private final LandlordCustomerService customerService;

    @Autowired
    public LandlordCustomerController(
            BrandingService brandingService,
            UserSecurityService userSecurityService,
            LandlordCustomerService customerService) {
        this.brandingService = brandingService;
        this.userSecurityService = userSecurityService;

        this.customerService = customerService;
    }

    @PostMapping(value = "/branding/validate/token")
    public ResponseEntity<Object> validateBrandingUrlToken(@RequestBody TokenBean tokenBean) {
        try {
            String token = tokenBean.getToken();
            BrandingUrlTokenData brandingUrlTokenData = brandingService.validateAndExtractBrandingUrlToken(token);
            return ResponseEntity.ok(brandingUrlTokenData);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return badRequest(TOKEN_NOT_VALID);
        }
    }

    @PatchMapping
    public ResponseEntity<Void> updateCustomer(@RequestBody LandlordCustomerBean customerBean) {
        customerService.updateCustomer(userSecurityService.getPrincipalUser().getCustomer(), customerBean);
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/branding/preview/generate-url")
    public ResponseEntity generatePreviewBrandingUrl(@RequestBody LandlordCustomerBranding customerBranding) {
        try {
            String brandingUrl = brandingService.generatePreviewBrandingUrl(customerBranding);
            return ResponseEntity.ok(brandingUrl);
        } catch (Exception ex) {
            return badRequest(ex.getMessage());
        }
    }

    @PostMapping(value = "/branding/preview/validate/token")
    public ResponseEntity<Object> validatePreviewBrandingUrlToken(@RequestBody TokenBean tokenBean) {
        try {
            String token = tokenBean.getToken();
            PreviewBrandingUrlToken brandingUrlToken = brandingService.validateAndExtractPreviewBrandingUrlToken(token);
            return ResponseEntity.ok(brandingUrlToken.getData());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return badRequest(TOKEN_NOT_VALID);
        }
    }

    @GetMapping(value = "/settings")
    public ResponseEntity settings(PersistentEntityResourceAssembler assembler) {
        try {
            LandlordUser landlordUser = userSecurityService.getPrincipalUser();
            LandlordCustomerSettings settings = landlordUser.getCustomer().getCustomerSettings();

            return new ResponseEntity<>(assembler.toModel(settings), HttpStatus.OK);
        } catch (Exception ex) {
            return badRequest(ex.getMessage());
        }
    }

}
