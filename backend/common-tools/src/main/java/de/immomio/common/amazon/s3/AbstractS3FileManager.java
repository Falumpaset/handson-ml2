package de.immomio.common.amazon.s3;

import com.google.common.io.Files;
import de.immomio.common.upload.FileStoreObject;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.base.type.common.FileType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static de.immomio.data.base.type.common.FileType.CREDIT_REPORT;
import static de.immomio.data.base.type.common.FileType.INCOME_STATEMENT;
import static de.immomio.data.base.type.common.FileType.PROOF_OF_PAYMENT;
import static de.immomio.data.base.type.common.FileType.SHARED_DOCUMENT;
import static de.immomio.data.base.type.common.FileType.WB_CERTIFICATE;

/**
 * @author Johannes Hiemer, Bastian Bliemeister, Maik Kingma.
 */
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class AbstractS3FileManager<T extends AbstractS3> {

    protected static boolean containsBucket(String bucket, String url) {
        return url != null && url.contains(bucket);
    }

    public abstract T getS3();

    public abstract String getBucketImg();

    public abstract String getBucketDoc();

    public abstract String getBucketSharedDoc();

    public void downloadFiles(List<S3File> s3Files, File folder) {
        s3Files.forEach(s3File -> downloadAttachment(s3File, folder));
    }

    private void downloadAttachment(S3File s3File, File folder) {
        try {
            String bucket = getBucketByObjectType(s3File.getType(), s3File.getUrl());
            getS3().downloadElement(
                    s3File.getType(),
                    bucket,
                    s3File.getIdentifier(),
                    s3File.getExtension(),
                    folder.getAbsolutePath());
        } catch (Exception ex) {
            log.error("Error downloading file", ex);
        }
    }

    public File downloadFileStoreObject(FileStoreObject fileStoreObject, File folder) {
        String bucket;
        try {
            bucket = getBucketByObjectType(fileStoreObject.getType(), fileStoreObject.getUrl());
        } catch (Exception ex) {
            log.error("Error downloading file", ex);

            return null;
        }

        return downloadFileStoreObject(fileStoreObject, folder, bucket);
    }

    public File downloadFileStoreObject(FileStoreObject fileStoreObject, File folder, String bucket) {
        File downloadedFile = null;

        try {
            downloadedFile = getS3().downloadElement(fileStoreObject.getType(), bucket, fileStoreObject.getIdentifier(),
                    fileStoreObject.getExtension(), folder.getAbsolutePath());
        } catch (Exception ex) {
            log.error("Error downloading file", ex);
        }

        return downloadedFile;
    }

    public File createTempFile(MultipartFile multipartFile, FileType fileType, String identifier, String extension)
            throws S3FileManagerException {
        return createTempFile(multipartFile, fileType, identifier, extension, Files.createTempDir());
    }

    public File createTempFile(MultipartFile multipartFile, FileType fileType, String identifier, String extension,
                               File folder) throws S3FileManagerException {
        File file = new File(folder + File.separator
                + AbstractS3.concatIdentifier(fileType, identifier, extension));
        try {
            multipartFile.transferTo(file);
        } catch (IllegalStateException | IOException e) {
            throw new S3FileManagerException(e);
        }

        return file;
    }

    public String getUrl(FileStoreObject fileStoreObject) throws MalformedURLException, S3FileManagerException {
        return getUrl(fileStoreObject.getType(), fileStoreObject.getIdentifier(), fileStoreObject.getExtension());
    }

    public String getUrl(FileType fileType, String identifier, String extension)
            throws S3FileManagerException, MalformedURLException {
        String bucket = getBucketByObjectType(fileType);

        return getUrl(bucket, fileType, identifier, extension);
    }

    public String getImageUrl(FileType fileType, String identifier, String extension)
            throws MalformedURLException {
        return getUrl(getBucketImg(), fileType, identifier, extension);
    }

    public String getDocumentUrl(FileType fileType, String identifier, String extension)
            throws MalformedURLException {
        return getUrl(getBucketDoc(), fileType, identifier, extension);
    }

    protected String getUrl(String bucket, FileType fileType, String identifier, String extension)
            throws MalformedURLException {

        URL url = getS3().generateUrl(bucket, fileType, identifier, extension);

        return url.toString();
    }

    public String uploadDocument(File file, FileType fileType, String identifier, String extension)
            throws S3FileManagerException {
        return uploadFile(file, fileType, identifier, extension, getBucketDoc());
    }

    public String uploadImage(File file, FileType fileType, String identifier, String extension)
            throws S3FileManagerException {
        return uploadFile(file, fileType, identifier, extension, getBucketImg());
    }

    public String uploadFile(File file, FileType fileType, String identifier, String extension)
            throws S3FileManagerException {
        String bucket = getBucketByObjectType(fileType);

        return uploadFile(file, fileType, identifier, extension, bucket);
    }

    public String uploadFile(File file, FileType fileType, String identifier, String extension, String bucket)
            throws S3FileManagerException {
        String returnFilename;
        try {
            returnFilename = getS3().storeElement(file, bucket, fileType, identifier, extension);
        } catch (Exception e) {
            throw new S3FileManagerException(e);
        }
        return returnFilename;
    }

    public boolean deleteDocument(FileType fileType, String identifier, String extension)
            throws S3FileManagerException {
        return deleteFile(fileType, identifier, extension, getBucketDoc());
    }

    public boolean deleteImage(FileType fileType, String identifier, String extension)
            throws S3FileManagerException {
        return deleteFile(fileType, identifier, extension, getBucketImg());
    }

    public boolean deleteFile(FileType fileType, String identifier, String extension, String bucket)
            throws S3FileManagerException {
        try {
            getS3().deleteElement(bucket, fileType, identifier, extension);
        } catch (Exception e) {
            throw new S3FileManagerException(e);
        }

        return true;
    }

    private String getBucketByObjectType(FileType type) throws S3FileManagerException {
        return getBucketByObjectType(type, null);
    }

    private String getBucketByObjectType(FileType type, String url) throws S3FileManagerException {
        // TODO Rückwärtskompatiblität bis zur vollständigen umstellung der Buckets.
        String bucket = getBucket(url);
        if (bucket != null && !bucket.isEmpty()) {
            return bucket;
        }

        switch (type) {
            case IMG:
                return getBucketImg();
            case PROOF_OF_PAYMENT:
            case CREDIT_REPORT:
            case WB_CERTIFICATE:
            case INCOME_STATEMENT:
            case SHARED_DOCUMENT:
                return getBucketSharedDoc();
            case PDF:
            case FLOOR_PLAN:
            case VIDEO:
            case ENERGY_CERTIFICATE:
            case SELF_ASSESSMENT:
                return getBucketDoc();
            default:
                throw new S3FileManagerException("Invalid URL -> " + url);
        }
    }

    public String getBucketByUrl(String url) throws S3FileManagerException {
        String bucket = getBucket(url);

        if (bucket == null || bucket.isEmpty()) {
            throw new S3FileManagerException("Invalid URL -> " + url);
        }

        return bucket;
    }

    public abstract String getBucket(String url);

    public boolean isImgBucket(String url) {
        return containsBucket(getBucketImg(), url);
    }

    public boolean isDocBucket(String url) {
        return containsBucket(getBucketDoc(), url);
    }

    public boolean isSharedDocBucket(String url) {
        return containsBucket(getBucketSharedDoc(), url);
    }

    public boolean isSharedFiles(FileType fileType) {
        return fileType == PROOF_OF_PAYMENT || fileType == CREDIT_REPORT || fileType == WB_CERTIFICATE ||
                fileType == INCOME_STATEMENT || fileType == SHARED_DOCUMENT;
    }
}
