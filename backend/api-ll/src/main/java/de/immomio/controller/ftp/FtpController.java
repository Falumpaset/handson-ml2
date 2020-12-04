package de.immomio.controller.ftp;

import de.immomio.exporter.config.FtpProperties;
import de.immomio.exporter.openimmo.upload.FtpUploader;
import de.immomio.exporter.openimmo.upload.FtpsUploader;
import de.immomio.exporter.openimmo.upload.SftpUploader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RepositoryRestController
@RequestMapping(value = "/ftp")
public class FtpController {

    private static final String CONNECTION_SUCCESS_MSG_KEY = "CONNECTION_SUCCESSFULLY_L";

    private static final String CONNECTION_FAILED_MSG_KEY = "CONNECTION_FAILED_L";

    @Autowired
    private FtpUploader ftpUploader;

    @Autowired
    private FtpsUploader ftpsUploader;

    @Autowired
    private SftpUploader sftpUploader;

    @PostMapping(value = "/check-connection")
    public ResponseEntity<Object> check(@RequestBody FtpProperties ftpProperties) {
        try {
            boolean connected = checkConnection(ftpProperties);
            if (connected) {
                return getResponse(CONNECTION_SUCCESS_MSG_KEY, HttpStatus.OK);
            } else {
                return getResponse(CONNECTION_FAILED_MSG_KEY, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return getResponse(CONNECTION_FAILED_MSG_KEY, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean checkConnection(FtpProperties properties) throws Exception {
        switch (properties.getType()) {
            case SFTP:
                return sftpUploader.checkConnection(properties.getHost(), properties.getPort(),
                        properties.getUsername(), properties.getPassword());
            case FTPS:
                return ftpsUploader.checkConnection(properties.getHost(), properties.getPort(),
                        properties.getUsername(), properties.getPassword());
            case FTP:
            default:
                return ftpUploader.checkConnection(properties.getHost(), properties.getPort(),
                        properties.getUsername(), properties.getPassword());
        }
    }

    private ResponseEntity<Object> getResponse(String message, HttpStatus status) {
        return new ResponseEntity<>(message, status);
    }
}
