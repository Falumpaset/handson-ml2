package de.immomio.controller.credential;

import de.immomio.exporter.AbstractExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bastian Bliemeister.
 */
@Controller
@RequestMapping(value = "/credentials")
public class CredentialController {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(CredentialController.class);

    @Autowired
    private List<AbstractExporter> exporters = new ArrayList<>();

    @RequestMapping(value = {"/check"}, method = RequestMethod.POST)
    public ResponseEntity checkCredentials(@RequestBody @Valid CredentialCheckBean credential) {
        Boolean bool = check(credential);

        if (bool == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else if (bool) {
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private Boolean check(CredentialCheckBean credential) {
        AbstractExporter exporter = null;
        for (AbstractExporter ae : exporters) {
            if (ae.getPortal() == credential.getPortal()) {
                exporter = ae;
                break;
            }
        }

        if (exporter == null) {
            return null;
        }

        try {
            return exporter.checkConnection(credential.toCredential());
        } catch (Exception e) {
            return false;
        }
    }
}
