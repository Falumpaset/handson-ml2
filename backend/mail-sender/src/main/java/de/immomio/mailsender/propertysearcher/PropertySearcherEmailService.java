package de.immomio.mailsender.propertysearcher;

import de.immomio.common.zip.FileZipper;
import de.immomio.mail.s3.MailPropertySearcherS3FileManager;
import de.immomio.mailsender.AbstractEmailService;
import de.immomio.mailsender.VelocityInternalizationLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @author Maik Kingma
 */

@Service
public class PropertySearcherEmailService extends AbstractEmailService {

    private final MailPropertySearcherS3FileManager mailPropertySearcherS3FileManager;

    @Value("${endpoints.filer.propertysearcher}")
    private String reportUrl;

    public PropertySearcherEmailService(
            Environment env,
            VelocityInternalizationLoader velocityInternationalizationLoader,
            FileZipper fileZipper,
            MailPropertySearcherS3FileManager mailPropertySearcherS3FileManager
    ) {
        super(env, velocityInternationalizationLoader, fileZipper);
        this.mailPropertySearcherS3FileManager = mailPropertySearcherS3FileManager;
    }


    @Override
    public MailPropertySearcherS3FileManager getS3FileManager() {
        return mailPropertySearcherS3FileManager;
    }

    @Override
    public String getFilerUrl() {
        return reportUrl;
    }
}
