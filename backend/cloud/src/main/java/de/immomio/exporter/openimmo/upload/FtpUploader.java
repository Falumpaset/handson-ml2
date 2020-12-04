/**
 *
 */
package de.immomio.exporter.openimmo.upload;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister.
 */
@Component
public class FtpUploader extends AbstractFtpUploader {

    private static final Logger log = LoggerFactory.getLogger(FtpUploader.class);

    private FTPClient ftpClient = null;

    @Value("${ftp.timeout}")
    private int timeout;

    public FtpUploader() {
        super();
    }

    @Override
    public boolean configure(String host, int port, String username, String password) throws IOException {
        boolean connected = true;

        ftpClient = new FTPClient();
        ftpClient.setDataTimeout(timeout);
        ftpClient.setConnectTimeout(timeout);
        ftpClient.setDefaultTimeout(timeout);
        ftpClient.connect(host, port);

        if (!ftpClient.login(username, password)) {
            log.warn("FTP login failed -> " + username + ":" + password + "@" + host + ":" + port);
            connected = false;
        } else if (!ftpClient.setFileType(FTP.BINARY_FILE_TYPE)) {
            log.warn("configure FTP failed -> " + host + ":" + port);
            connected = false;
        }

        ftpClient.enterLocalPassiveMode();

        if (!connected && ftpClient.isConnected()) {
            ftpClient.logout();
            ftpClient.disconnect();
        }

        return connected;
    }

    @Override
    public boolean checkConnection(String host, int port, String username, String password) throws IOException {
        boolean connected = false;

        FTPClient client = new FTPClient();
        client.connect(host, port);

        if (client.isConnected()) {
            connected = client.login(username, password);
            client.disconnect();
        }

        return connected;
    }

    @Override
    public boolean upload(String sourceFilePath, String destinationFile) {
        boolean done = false;
        File localFile = new File(sourceFilePath);

        try (InputStream inputStream = new FileInputStream(localFile)) {
            log.info("Start uploading first file");
            log.info("Uploading file: " + localFile.getAbsolutePath());

            done = ftpClient.storeFile(destinationFile, inputStream);
            if (done) {
                log.info("Successfully uploaded file via FTP");
            } else {
                log.warn("Unsuccessfully uploaded file via FTP");
            }
        } catch (IOException ex) {
            log.error("Exception during FTP upload", ex);
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                log.error("Exception during FTP connection handling", ex);
            }
        }

        return done;
    }
}
