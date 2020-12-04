package de.immomio.exporter.amazon.s3;

import com.amazonaws.services.s3.AmazonS3;
import de.immomio.common.amazon.s3.AbstractS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Maik Kingma
 */

@Service
public class BrokerS3 extends AbstractS3 {

    @Value("${s3.endpoint}")
    private String endpoint;

    @Autowired
    private BrokerS3Config s3Config;

    @Override
    public String getEndpoint() {
        return this.endpoint;
    }

    @Override
    public AmazonS3 getClient() {
        return s3Config.getClient();
    }

}
