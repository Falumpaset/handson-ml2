package de.immomio.importer;

import de.immomio.importer.server.CustomServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author Johannes Hiemer.
 */
@Component
public class CustomCommandLineRunner implements CommandLineRunner {

    @Autowired
    private CustomServer customFtpServer;

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
     */
    @Override
    public void run(String... args) throws Exception {
        this.customFtpServer.startServer();
        this.customFtpServer.startSftpServer();
    }

}
