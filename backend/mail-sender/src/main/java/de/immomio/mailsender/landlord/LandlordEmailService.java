package de.immomio.mailsender.landlord;

import de.immomio.common.zip.FileZipper;
import de.immomio.mail.s3.MailLandlordS3FileManager;
import de.immomio.mailsender.AbstractEmailService;
import de.immomio.mailsender.VelocityInternalizationLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @author Maik Kingma
 */

@Service
public class LandlordEmailService extends AbstractEmailService {

    private final MailLandlordS3FileManager mailLandlordS3FileManager;

    @Value("${endpoints.filer.landlord}")
    private String reportUrl;

    @Autowired
    public LandlordEmailService(
            Environment env,
            VelocityInternalizationLoader velocityInternationalizationLoader,
            FileZipper fileZipper,
            MailLandlordS3FileManager mailLandlordS3FileManager
    ) {
        super(env, velocityInternationalizationLoader, fileZipper);
        this.mailLandlordS3FileManager = mailLandlordS3FileManager;
    }


    @Override
    public MailLandlordS3FileManager getS3FileManager() {
        return mailLandlordS3FileManager;
    }

    @Override
    public String getFilerUrl() {
        return reportUrl;
    }
}
