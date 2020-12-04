package de.immomio.controller;

import de.immomio.common.amazon.s3.AbstractS3FileManager;
import de.immomio.data.base.type.common.FileType;
import de.immomio.utils.FileStorageUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public abstract class AbstractFilerController<S3FileManager extends AbstractS3FileManager> {

    public static final String URL_PARAM = "url";

    @Value("${encryption.files.key}")
    private String encryptionKey;

    @Value("${encryption.files.shared.key}")
    private String encryptionKeyForSharedFiles;

    public ResponseEntity download(S3FileManager s3FileManager, String url, String filename, boolean encrypted,
                                   HttpServletRequest request) throws IOException {
        if (url == null) {
            url = request.getHeader(URL_PARAM);
        }

        if (url == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (Base64.isBase64(url)) {
            url = new String(Base64.decodeBase64(url));
        }

        String encryptionKey = getEncryptionKeyByUrl(s3FileManager, url);
        return FileStorageUtils.getFile(url, filename, s3FileManager, encrypted, encryptionKey);
    }

    public String getEncryptionKeyByFileType(S3FileManager s3FileManager, FileType fileType) {
        return s3FileManager.isSharedFiles(fileType) ? encryptionKeyForSharedFiles : encryptionKey;
    }

    public String getEncryptionKeyByUrl(S3FileManager s3FileManager, String url) {
        return s3FileManager.isSharedDocBucket(url) ? encryptionKeyForSharedFiles : encryptionKey;
    }
}
