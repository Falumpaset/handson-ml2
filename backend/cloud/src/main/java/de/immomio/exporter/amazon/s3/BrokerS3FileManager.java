package de.immomio.exporter.amazon.s3;

import de.immomio.common.amazon.s3.AbstractS3FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author Maik Kingma
 */

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BrokerS3FileManager extends AbstractS3FileManager {

    @Autowired
    private BrokerS3 brokerS3;

    @Value("${s3.landlord.bucket-img}")
    private String BUCKET_IMG;

    @Value("${s3.landlord.bucket-doc}")
    private String BUCKET_DOC;

    @Override
    public String getBucket(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        } else if (isImgBucket(url)) {
            return getBucketImg();
        } else if (isDocBucket(url)) {
            return getBucketDoc();
        }
        return null;
    }

    @Override
    public BrokerS3 getS3() {
        return this.brokerS3;
    }

    @Override
    public String getBucketImg() {
        return this.BUCKET_IMG;
    }

    @Override
    public String getBucketDoc() {
        return this.BUCKET_DOC;
    }

    @Override
    public String getBucketSharedDoc() {
        return null;
    }

}
