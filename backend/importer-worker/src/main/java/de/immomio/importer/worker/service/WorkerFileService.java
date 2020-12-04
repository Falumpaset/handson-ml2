package de.immomio.importer.worker.service;

import com.google.common.io.Files;
import de.immomio.common.amazon.s3.S3FileManagerException;
import de.immomio.common.encryption.exception.CryptoException;
import de.immomio.common.file.FileUtilities;
import de.immomio.common.upload.FileStoreObject;
import de.immomio.common.upload.FileStoreService;
import de.immomio.common.zip.FileZipper;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.importer.worker.s3.ImporterWorkerS3;
import de.immomio.importer.worker.s3.ImporterWorkerS3FileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */

@Service
@Slf4j
public class WorkerFileService {

    @Value("${filestore.encryption.key}")
    private String encryptionKey;

    @Autowired
    private ImporterWorkerS3FileManager s3FileManager;

    @Autowired
    private FileZipper fileZipper;

    File download(String url, boolean isEncrypted) throws IOException {
        File folder = Files.createTempDir();

        FileStoreObject fileStoreObject = FileStoreObject.parse(url);

        File downloadedFile = s3FileManager.downloadFileStoreObject(fileStoreObject, folder);

        if (isEncrypted) {
            File decryptedFile = null;
            try {
                decryptedFile = FileStoreService.decryptFile(downloadedFile, encryptionKey);
                FileUtilities.forceDelete(downloadedFile.getParentFile());
            } catch (CryptoException e) {
                throw new IOException("Unable to decrypt file", e);
            }
            return decryptedFile;
        }

        return downloadedFile;
    }

    public String getFilename(S3File attachment) {
        return ImporterWorkerS3.concatIdentifier(attachment.getType(), attachment.getIdentifier(),
                attachment.getExtension());
    }

    File unzip(File file) {
        File folder = Files.createTempDir();

        fileZipper.unzipFile(file.getAbsolutePath(), folder);

        return folder;
    }

    String uploadImage(File file, S3File attachment) throws S3FileManagerException {
        return s3FileManager.uploadImage(file, attachment.getType(), attachment.getIdentifier(),
                attachment.getExtension());
    }

    String uploadDocument(File file, S3File attachment) throws S3FileManagerException {
        return s3FileManager.uploadDocument(file, attachment.getType(), attachment.getIdentifier(),
                attachment.getExtension());
    }

    void deleteImages(List<S3File> attachments) {
        for (S3File attachment : attachments) {
            try {
                deleteImage(attachment);
            } catch (Exception e) {
                log.error("Error deleting Image.", e);
            }
        }
    }

    private boolean deleteImage(S3File attachment) throws S3FileManagerException {
        return s3FileManager.deleteImage(attachment.getType(), attachment.getIdentifier(), attachment.getExtension());
    }

    void deleteDocuments(List<S3File> attachments) {
        for (S3File attachment : attachments) {
            try {
                deleteDocument(attachment);
            } catch (Exception e) {
                log.error("Error deleting Document.", e);
            }
        }
    }

    private boolean deleteDocument(S3File attachment) throws S3FileManagerException {
        return s3FileManager.deleteDocument(attachment.getType(), attachment.getIdentifier(),
                attachment.getExtension());
    }

    boolean deleteImportFile(S3File attachment) throws S3FileManagerException {
        return s3FileManager.deleteImportFile(attachment.getType(), attachment.getIdentifier(),
                attachment.getExtension());
    }

}
