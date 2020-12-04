package de.immomio.exporter.openimmo.upload;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPSClient;
import org.apache.commons.net.util.TrustManagerUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component
@NoArgsConstructor
public class FtpsUploader extends AbstractFtpUploader {

    public static final int PROTECTION_BUFFER_SIZE = 0;
    public static final String PRIVATE = "P";

    private FTPSClient ftpsClient = null;

    @Value("${ftp.timeout}")
    private int timeout;

    @Override
    public boolean configure(String host, int port, String username, String password) throws IOException {

        boolean connected = true;

        ftpsClient = getFtpsClient();
        ftpsClient.connect(host, port);
        ftpsClient.execPBSZ(PROTECTION_BUFFER_SIZE);
        ftpsClient.execPROT(PRIVATE);

        if (!ftpsClient.login(username, password)) {
            log.warn("FTPS login failed -> " + username + ":" + password + "@" + host + ":" + port);
            connected = false;
        } else if (!ftpsClient.setFileType(FTP.BINARY_FILE_TYPE)) {
            log.warn("configure FTPS failed -> " + host + ":" + port);
        }

        ftpsClient.enterLocalPassiveMode();

        if (!connected && ftpsClient.isConnected()) {
            ftpsClient.logout();
            ftpsClient.disconnect();
        }

        return connected;
    }

    @Override
    public boolean checkConnection(String host, int port, String username, String password) throws IOException {
        boolean connected = false;

        FTPSClient client = getFtpsClient();
        client.connect(host, port);


        if (client.isConnected()) {
            connected = client.login(username, password);
            client.disconnect();
        }

        return connected;
    }

    public boolean upload(String sourceFilePath, String destinationFile) {
        boolean done = false;
        File localFile = new File(sourceFilePath);

        try (InputStream inputStream = new FileInputStream(localFile)) {
            log.info("Start uploading first file");
            log.info("Uploading file: " + localFile.getAbsolutePath());

            done = ftpsClient.storeFile(destinationFile, inputStream);
            if (done) {
                log.info("Successfully uploaded file via FTPS");
            } else {
                log.warn("Unsuccessfully uploaded file via FTPS");
            }
        } catch (IOException ex) {
            log.error("Exception during FTP upload", ex);
        } finally {
            try {
                if (ftpsClient.isConnected()) {
                    ftpsClient.logout();
                    ftpsClient.disconnect();
                }
            } catch (IOException ex) {
                log.error("Exception during FTPS connection handling", ex);
            }
        }

        return done;
    }

    private FTPSClient getFtpsClient() {
        FTPSClient client = new FTPSClient();
        client.setTrustManager(TrustManagerUtils.getAcceptAllTrustManager());
        client.setDataTimeout(timeout);
        client.setConnectTimeout(timeout);
        client.setDefaultTimeout(timeout);

        return client;
    }
}
