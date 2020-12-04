package de.immomio.recipient.service.amazon.s3;

import de.immomio.common.amazon.s3.AbstractS3FileManager;
import de.immomio.common.amazon.s3.S3FileManagerException;
import de.immomio.data.base.type.common.FileType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author Fabian Beck.
 */

@Slf4j
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class RecipientS3FileManager extends AbstractS3FileManager {

    private final RecipientS3 recipientS3;

    @Value("${s3.shared.bucket-doc}")
    private String sharedDocBucket;

    @Autowired
    public RecipientS3FileManager(RecipientS3 recipientS3) {
        this.recipientS3 = recipientS3;
    }

    @Override
    public RecipientS3 getS3() {
        return this.recipientS3;
    }

    @Override
    public String getBucketImg() {
        return null;
    }

    @Override
    public String getBucketDoc() {
        return null;
    }

    @Override
    public String getBucketSharedDoc() {
        return sharedDocBucket;
    }

    @Override
    public String getBucket(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        } else if (isSharedDocBucket(url)) {
            return sharedDocBucket;
        }
        return null;
    }

    public String uploadAttachment(File file, FileType fileType, String identifier, String extension) throws
            S3FileManagerException {
        return uploadFile(file, fileType, identifier, extension, sharedDocBucket);
    }
}
