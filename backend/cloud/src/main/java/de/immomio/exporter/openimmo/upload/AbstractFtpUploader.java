package de.immomio.exporter.openimmo.upload;

import com.jcraft.jsch.JSchException;

import java.io.IOException;

public abstract class AbstractFtpUploader {

    public abstract boolean checkConnection(String host, int port, String username, String password)
            throws IOException, JSchException;

    public abstract boolean configure(String host, int port, String username, String password) throws IOException;

    public abstract boolean upload(String sourceFilePath, String destinationFile);
}
