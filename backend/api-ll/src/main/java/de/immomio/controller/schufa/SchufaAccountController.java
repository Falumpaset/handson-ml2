package de.immomio.controller.schufa;

import de.immomio.beans.landlord.CreateSchufaAccountBean;
import de.immomio.constants.exceptions.EncryptionException;
import de.immomio.landlord.service.security.UserSecurityService;
import de.immomio.schufa.SchufaAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Niklas Lindemann
 */

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/schufaAccounts")
public class SchufaAccountController {

    private SchufaAccountService schufaAccountService;

    private UserSecurityService securityService;

    @Value("${encryption.credentials.key}")
    private String encryptionKey;

    @Autowired
    public SchufaAccountController(SchufaAccountService schufaAccountService, UserSecurityService securityService) {
        this.schufaAccountService = schufaAccountService;
        this.securityService = securityService;
    }

    @PostMapping("/save")
    public ResponseEntity createSchufaAccount(@RequestBody CreateSchufaAccountBean accountBean) {
        try {
            schufaAccountService.createSchufaAccount(
                    securityService.getPrincipalUser().getCustomer(),
                    accountBean.getUsername(),
                    accountBean.getPassword());
        } catch (EncryptionException e) {
           log.error(e.getMessage(), e);
           return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.ok().build();
    }

}
