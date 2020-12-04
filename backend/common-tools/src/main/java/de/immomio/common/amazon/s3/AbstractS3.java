/**
 *
 */
package de.immomio.common.amazon.s3;

import com.amazonaws.services.s3.AbstractAmazonS3;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import de.immomio.data.base.type.common.FileType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 * <p>
 * Create a public bucket: { "Version": "2008-10-17", "Id": "Policy1410846366931", "Statement": [ { "Sid":
 * "Stmt1410846362554", "Effect": "Allow", "Principal": { "AWS": "*" }, "Action": [ "s3:DeleteObject", "s3:GetObject",
 * "s3:PutObject" ], "Resource": "arn:aws:s3:::whibs/*" } ] }
 */
@Slf4j
public abstract class AbstractS3 extends AbstractAmazonS3 {

    public static String concatIdentifier(FileType fileType, String identifier, String extension) {
        if (fileType == null) {
            throw new IllegalArgumentException("objectType may not be null");
        } else if (identifier == null) {
            throw new IllegalArgumentException("identifier may not be null");
        } else if (extension == null) {
            throw new IllegalArgumentException("extension may not be null");
        }

        String objectType;

        switch (fileType) {
            case IMG:
                objectType = FileType.IMG.name();
                break;
            case VIDEO:
                objectType = FileType.VIDEO.name();
                break;
            case MAILATTACHMENTS:
                objectType = FileType.MAILATTACHMENTS.name();
                break;
            case ZIP:
                objectType = FileType.ZIP.name();
                break;
            case ENERGY_CERTIFICATE:
            case WB_CERTIFICATE:
            case CREDIT_REPORT:
            case SELF_ASSESSMENT:
            case INCOME_STATEMENT:
            case PROOF_OF_PAYMENT:
            case SHARED_DOCUMENT:
                objectType = FileType.SHARED_DOCUMENT.name();
                break;
            case FLOOR_PLAN:
            case PDF:
            default:
                objectType = FileType.PDF.name();
                break;
        }

        return objectType + "-" + identifier + "." + extension;
    }

    public abstract String getEndpoint();

    public abstract AmazonS3 getClient();

    public String storeElement(File file, String bucket, FileType fileType, String identifier, String extension)
            throws IOException {
        Assert.notNull(bucket, "Bucket may not be undefined");

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.addUserMetadata("objectType", fileType.name());
        objectMetadata.addUserMetadata("identifier", identifier);
        objectMetadata.addUserMetadata("extension", extension);

        String filename = AbstractS3.concatIdentifier(fileType, identifier, extension);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, filename, file);
        putObjectRequest.setMetadata(objectMetadata);
        getClient().putObject(putObjectRequest);

        URL url = generateUrl(bucket, filename);

        log.info("Uploaded file to: " + url.toString());

        return url.toString();
    }

    public URL generateUrl(String bucket, FileType fileType, String identifier, String extension)
            throws MalformedURLException {
        String filename = AbstractS3.concatIdentifier(fileType, identifier, extension);

        return generateUrl(bucket, filename);
    }

    public URL generateUrl(String bucket, String filename) throws MalformedURLException {

        return new URL("https", getEndpoint(), "/" + bucket + "/" + filename);
    }

    public File downloadElement(FileType fileType, String bucket, String identifier, String extension, String path)
            throws IOException, IllegalArgumentException {
        Assert.notNull(getClient(), "S3 Connection may not be null");
        Assert.notNull(bucket, "Bucket may not be undefined");

        if (fileType == null && identifier == null && extension == null) {
            throw new IllegalArgumentException("ObjectType/Identifier/Extension may not be null");
        } else if (!getClient().doesBucketExistV2(bucket)) {
            throw new IllegalArgumentException("Bucket does not exists -> " + bucket);
        }

        String filename = AbstractS3.concatIdentifier(fileType, identifier, extension);

        if (!getClient().doesObjectExist(bucket, filename)) {
            throw new IllegalArgumentException("File does not exists -> " + bucket + " -> " + filename);
        }

        File downloadedFile = new File(path + File.separator + filename);
        S3Object s3Object = getClient().getObject(new GetObjectRequest(bucket, filename));

        try (InputStream inputStream = new BufferedInputStream(s3Object.getObjectContent());
             OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadedFile))) {
            log.info("Opening file save to: " + downloadedFile.getAbsolutePath());

            int read;
            while ((read = inputStream.read()) != -1) {
                outputStream.write(read);
            }
        }

        log.info("Saved file to: " + downloadedFile.getAbsolutePath());

        return downloadedFile;
    }

    public void deleteElement(String bucket, FileType fileType, String identifier, String extension) {

        String filename = AbstractS3.concatIdentifier(fileType, identifier, extension);

        getClient().deleteObject(bucket, filename);

        log.info("File deleted: " + bucket + "/" + filename);
    }
}
