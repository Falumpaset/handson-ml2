package de.immomio.utils;

import com.google.common.io.Files;
import de.immomio.common.amazon.s3.AbstractS3FileManager;
import de.immomio.common.encryption.exception.CryptoException;
import de.immomio.common.file.DeleteOnCloseFileInputStream;
import de.immomio.common.file.FileUtilities;
import de.immomio.common.upload.FileStoreObject;
import de.immomio.common.upload.FileStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class FileStorageUtils {

    public static ResponseEntity getFile(String url, String filename, AbstractS3FileManager s3FileManager,
                                         boolean encrypted, String encryptionKey) throws IOException {

        File file = downloadFile(url, s3FileManager, encrypted, encryptionKey);
        String mime;
        try {
            mime = FileUtilities.detectMimetype(file);
        } catch (Exception e) {
            mime = null;
        }

        if (mime == null || mime.isEmpty()) {
            mime = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        if (file == null) {
            throw new IllegalArgumentException("File not found by URL");
        }

        ResponseEntity.BodyBuilder bb = ResponseEntity.ok().contentLength(file.length())
                .contentType(MediaType.parseMediaType(mime));

        String downloadName = filename != null && !filename.isEmpty() ? filename : file.getName();
        bb.header("Content-Disposition", "inline;filename=" + downloadName);

        try (InputStream inputStream = new DeleteOnCloseFileInputStream(file, true)) {
            return bb.body(new InputStreamResource(inputStream));
        }
    }

    public static File downloadFile(String url, AbstractS3FileManager s3FileManager, boolean encrypted, String encryptionKey) throws IOException {
        File folder = Files.createTempDir();
        FileStoreObject fileStoreObject = FileStoreObject.parse(url);
        File downloadedFile = s3FileManager.downloadFileStoreObject(fileStoreObject, folder);
        File file;

        if (encrypted) {
            try {
                file = FileStoreService.decryptFile(downloadedFile, encryptionKey);
                FileUtilities.forceDelete(downloadedFile.getParentFile());
            } catch (CryptoException ex) {
                throw new IOException("Unable to decrypt file", ex);
            }
        } else {
            file = downloadedFile;
        }

        return file;
    }


}
