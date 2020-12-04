package de.immomio.mail.s3;

import de.immomio.common.amazon.s3.AbstractS3FileManager;
import de.immomio.common.amazon.s3.S3FileManagerException;
import de.immomio.data.base.type.common.FileType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MailLandlordS3FileManager extends AbstractS3FileManager {

    @Autowired
    private MailLandlordS3 landlordS3;

    @Value("${s3.landlord.bucket-img}")
    private String BUCKET_IMG;

    @Value("${s3.landlord.bucket-doc}")
    private String BUCKET_DOC;

    @Getter
    @Value("${s3.landlord.bucket-email}")
    private String BUCKET_MAIL;

    public String getMailFileUrl(FileType fileType, String identifier, String extension)
            throws MalformedURLException {
        return getUrl(BUCKET_MAIL, fileType, identifier, extension);
    }

    public boolean deleteMailFile(FileType fileType, String identifier, String extension)
            throws S3FileManagerException {
        return deleteFile(fileType, identifier, extension, BUCKET_MAIL);
    }

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
    public MailLandlordS3 getS3() {
        return landlordS3;
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
        return null;
    }
}
