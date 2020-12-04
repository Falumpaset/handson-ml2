package de.immomio.mail.s3;

import com.amazonaws.services.s3.AmazonS3;
import de.immomio.common.amazon.s3.AbstractS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service

public class MailPropertySearcherS3 extends AbstractS3 {

    @Autowired
    private MailPropertySearcherS3Config s3Config;

    @Value("${s3.endpoint}")
    private String endpoint;

    @Override
    public String getEndpoint() {
        return this.endpoint;
    }

    @Override
    public AmazonS3 getClient() {
        return s3Config.getClient();
    }

}
