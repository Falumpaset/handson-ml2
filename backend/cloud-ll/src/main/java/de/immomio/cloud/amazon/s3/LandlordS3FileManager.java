package de.immomio.cloud.amazon.s3;

import de.immomio.common.amazon.s3.AbstractS3FileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister.
 */
@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class LandlordS3FileManager extends AbstractS3FileManager {

    private final LandlordS3 landlordS3;

    @Value("${s3.landlord.bucket-img}")
    private String BUCKET_IMG;

    @Value("${s3.landlord.bucket-doc}")
    private String BUCKET_DOC;

    @Value("${s3.shared.bucket-doc}")
    private String sharedDocBucket;

    @Autowired
    public LandlordS3FileManager(LandlordS3 landlordS3) {
        this.landlordS3 = landlordS3;
    }

    @Override
    public LandlordS3 getS3() {
        return this.landlordS3;
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
        return sharedDocBucket;
    }

    @Override
    public String getBucket(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        } else if (isImgBucket(url)) {
            return BUCKET_IMG;
        } else if (isDocBucket(url)) {
            return BUCKET_DOC;
        } else if (isSharedDocBucket(url)) {
            return sharedDocBucket;
        }
        return null;
    }
}
