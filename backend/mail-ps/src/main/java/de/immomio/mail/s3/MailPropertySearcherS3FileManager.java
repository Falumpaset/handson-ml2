package de.immomio.mail.s3;

import de.immomio.common.amazon.s3.AbstractS3FileManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MailPropertySearcherS3FileManager extends AbstractS3FileManager {

    @Autowired
    private MailPropertySearcherS3 mailPropertySearcherS3;

    @Value("${s3.propertySearcher.bucket-img}")
    private String BUCKET_IMG;

    @Value("${s3.propertySearcher.bucket-doc}")
    private String BUCKET_DOC;

    @Getter
    @Value("${s3.propertySearcher.bucket-email}")
    private String BUCKET_MAIL;

    @Value("${s3.shared.bucket-doc}")
    private String sharedDocBucket;

    @Override
    public String getBucket(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        } else if (isImgBucket(url)) {
            return getBucketImg();
        } else if (isDocBucket(url)) {
            return getBucketDoc();
        } else if (isMailBucket(url)) {
            return BUCKET_MAIL;
        }
        return null;
    }

    public boolean isMailBucket(String url) {
        return containsBucket(BUCKET_MAIL, url);
    }

    @Override
    public MailPropertySearcherS3 getS3() {
        return mailPropertySearcherS3;
    }

    @Override
    public String getBucketImg() {
        return BUCKET_IMG;
    }

    @Override
    public String getBucketDoc() {
        return BUCKET_DOC;
    }

    @Override
    public String getBucketSharedDoc() {
        return sharedDocBucket;
    }

}
