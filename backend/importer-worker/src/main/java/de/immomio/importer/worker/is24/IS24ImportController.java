package de.immomio.importer.worker.is24;

import de.immomio.data.landlord.entity.credential.Credential;
import de.immomio.exporter.utils.credential.CredentialUtils;
import de.immomio.model.repository.service.landlord.customer.credential.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class IS24ImportController {

    @Autowired
    private Immoscout24Importer i24i;

    @Autowired
    private CredentialRepository baseCredentialRepository;

    @Value("${encryption.credentials.key}")
    private String encryptionKey;

    @RequestMapping(value = "/import/is24", method = RequestMethod.GET)
    public ResponseEntity<Object> IS24Import(@RequestParam("userid") long userID,
                                             @RequestParam("customerid") long customerId,
                                             @RequestParam("credentialid") long credentialId,
                                             @RequestParam("max") int max) {

        Optional<Credential> credentialOpt = baseCredentialRepository.findById(credentialId);

        if (credentialOpt.isPresent()) {
            Credential credential = credentialOpt.get();
            String token = CredentialUtils.getToken(credential, encryptionKey);
            String tokenSecret = CredentialUtils.getTokenSecret(credential, encryptionKey);

            i24i.init();
            i24i.login(token, tokenSecret);
            i24i.importAll(userID, customerId, max);

            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
