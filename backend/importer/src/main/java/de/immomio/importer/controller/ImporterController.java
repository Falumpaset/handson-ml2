package de.immomio.importer.controller;

import de.immomio.importer.server.CustomServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.ftpserver.ftplet.FtpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Bastian Bliemeister.
 */
@Slf4j
@Controller
public class ImporterController {

    private final CustomServer customFtpServer;

    @Autowired
    public ImporterController(CustomServer customFtpServer) {
        this.customFtpServer = customFtpServer;
    }

    @RequestMapping(value = "/reloadUsers", method = RequestMethod.GET)
    public ResponseEntity<EntityModel<Object>> importObject(HttpServletRequest request, HttpServletResponse response) {
        try {
            setupUsers();
        } catch (FtpException e) {
            log.error("Error reloading FTP-Users.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    private void setupUsers() throws FtpException {
        customFtpServer.setupUser();
    }
}
