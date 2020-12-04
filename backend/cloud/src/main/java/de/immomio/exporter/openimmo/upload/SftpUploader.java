package de.immomio.exporter.openimmo.upload;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@Component
@NoArgsConstructor
public class SftpUploader extends AbstractFtpUploader {

    private Session session;

    @Value("${ftp.timeout}")
    private int timeout;

    @Override
    public boolean checkConnection(String host, int port, String username, String password)
            throws IOException, JSchException {
        JSch jsch = new JSch();
        Session checkSession = jsch.getSession(username, host, port);
        checkSession.setPassword(password);
        checkSession.setConfig(getConfig());

        checkSession.connect();
        boolean connected = checkSession.isConnected();
        checkSession.disconnect();

        return connected;
    }

    @Override
    public boolean configure(String host, int port, String username, String password) throws IOException {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            session.setConfig(getConfig());
            session.setTimeout(timeout);

            session.connect();
            return session.isConnected();
        } catch (JSchException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean upload(String sourceFilePath, String destinationFile) {
        Channel channel = null;
        File localFile = new File(sourceFilePath);

        try (InputStream inputStream = new FileInputStream(localFile)) {
            log.info("Start uploading first file");
            log.info("Uploading file: " + localFile.getAbsolutePath());

            channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp channelSftp = (ChannelSftp) channel;

            channelSftp.put(inputStream, destinationFile);
            log.info("Successfully uploaded file via SFTP");
            return true;
        } catch (Exception ex) {
            log.error("Exception during FTP upload", ex);
        } finally {
            try {
                if (session.isConnected()) {
                    if (channel != null) {
                        channel.disconnect();
                    }
                    session.disconnect();
                }
            } catch (Exception ex) {
                log.error("Exception during FTP connection handling", ex);
            }
        }

        return false;
    }

    private Properties getConfig() {
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");

        return config;
    }
}
