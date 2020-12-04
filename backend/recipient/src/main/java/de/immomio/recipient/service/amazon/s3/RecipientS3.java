package de.immomio.recipient.service.amazon.s3;

import com.amazonaws.services.s3.AmazonS3;
import de.immomio.common.amazon.s3.AbstractS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Fabian Beck.
 */

@Service
public class RecipientS3 extends AbstractS3 {

    @Value("${s3.endpoint}")
    private String endpoint;

    @Autowired
    private RecipientS3Config s3Config;

    @Override
    public String getEndpoint() {
        return this.endpoint;
    }

    @Override
    public AmazonS3 getClient() {
        return s3Config.getClient();
    }

}
