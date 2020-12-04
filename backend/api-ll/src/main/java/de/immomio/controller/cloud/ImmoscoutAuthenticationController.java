package de.immomio.controller.cloud;

import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.entity.credential.Credential;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.exporter.immoscout.Immoscout24OAuth;
import de.immomio.model.listener.CredentialOnBeforeSaveListener;
import de.immomio.model.repository.landlord.customer.LandlordCustomerRepository;
import de.immomio.model.repository.landlord.customer.credential.LandlordCredentialRepository;
import lombok.extern.slf4j.Slf4j;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;

/**
 * Handles requests for the application home page.
 */
@Slf4j
@Controller
@RequestMapping(value = "/cloud/immoscout")
public class ImmoscoutAuthenticationController {

    @Autowired
    private Immoscout24OAuth immoscoutOAuthService;

    @Autowired
    private LandlordCustomerRepository customerRepository;

    @Autowired
    private LandlordCredentialRepository credentialRepository;

    @Autowired
    private CredentialOnBeforeSaveListener credentialOnBeforeSaveListener;

    @RequestMapping(value = "/oauth", method = RequestMethod.GET)
    public Object oauth() throws OAuthMessageSignerException, OAuthNotAuthorizedException,
            OAuthExpectationFailedException, OAuthCommunicationException {

        return "redirect:" + immoscoutOAuthService.preAuth();
    }

    @RequestMapping(value = "/oauth/confirm/portal/{pin}", method = RequestMethod.GET)
    public ResponseEntity<Object> configPortal(@PathVariable("pin") String pin,
                                               @RequestParam Long customerId,
                                               @RequestParam(required = false) String name) {
        return config(pin, customerId, name, Portal.IMMOBILIENSCOUT24_DE, null);
    }

    @RequestMapping(value = "/oauth/confirm/module/{pin}", method = RequestMethod.GET)
    public ResponseEntity<Object> configModule(@PathVariable("pin") String pin,
                                               @RequestParam Long customerId,
                                               @RequestParam(required = false) String name) {

        return config(pin, customerId, name, Portal.IMMOBILIENSCOUT24_HM_DE, null);
    }

    @RequestMapping(value = "/oauth/confirm/custom/{pin}", method = RequestMethod.GET)
    public ResponseEntity<Object> configCustom(@PathVariable("pin") String pin,
                                               @RequestParam Long customerId,
                                               @RequestParam(required = false) String name,
                                               @RequestParam Long channel) {

        return config(pin, customerId, name, Portal.IMMOBILIENSCOUT24_GC_DE, channel);
    }

    private ResponseEntity<Object> config(String pin, Long customerId, String name, Portal type, Long channel) {
        Optional<LandlordCustomer> customerOpt = customerRepository.findById(customerId);

        if (customerOpt.isPresent()) {
            LandlordCustomer customer = customerOpt.get();
            Map<String, String> newImmoscoutCredentials;

            try {
                newImmoscoutCredentials = immoscoutOAuthService.postAuth(pin);
            } catch (Exception ex) {
                log.error(ex.getMessage());
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }

            Credential credential = new Credential();
            credential.setName(name != null ? name : "");
            credential.setPortal(type);
            credential.setCustomer(customer);
            credential.setProperties(newImmoscoutCredentials);

            credentialOnBeforeSaveListener.handleBeforeSave(credential);

            if (channel != null) {
                credential.getProperties().put("channel", channel.toString());
            }
            credentialRepository.save(credential);

            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
