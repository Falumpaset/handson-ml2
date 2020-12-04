package de.immomio.common.amazon.s3;

import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractS3ImporterFileManager<T extends AbstractS3> extends AbstractS3FileManager<T> {

    @Value("${s3.landlord.bucket-img}")
    private String BUCKET_IMG;

    @Value("${s3.landlord.bucket-doc}")
    private String BUCKET_DOC;

    @Value("${s3.landlord.bucket-imp}")
    private String BUCKET_IMP;

    private boolean isImportBucket(String url) {
        return containsBucket(BUCKET_IMP, url);
    }

    @Override
    public String getBucket(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        } else if (isImgBucket(url)) {
            return BUCKET_IMG;
        } else if (isDocBucket(url)) {
            return BUCKET_DOC;
        } else if (isImportBucket(url)) {
            return BUCKET_IMP;
        }
        return null;
    }

    public String getBucketImp() {
        return BUCKET_IMP;
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
