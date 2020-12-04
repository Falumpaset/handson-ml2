package de.immomio.importer.server.handler.util;

import de.immomio.common.amazon.s3.S3FileManagerException;
import de.immomio.common.file.FileUtilities;
import de.immomio.data.base.type.common.FileType;
import de.immomio.importer.s3.ImporterS3FileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author Maik Kingma
 */

@Slf4j
@Service
public class AsyncUploadExecutor {

    private final ImporterS3FileManager s3FileManager;

    @Autowired
    public AsyncUploadExecutor(ImporterS3FileManager s3FileManager) {
        this.s3FileManager = s3FileManager;
    }

    private CompletableFuture<String> uploadZip(File zipFile, String identifier) {
        FileType type = FileType.ZIP;
        String extension = "zip";
        return CompletableFuture.supplyAsync(() -> {
            String zipUrl = null;
            try {
                zipUrl = s3FileManager.uploadImportFile(zipFile, type, identifier, extension);
            } catch (S3FileManagerException e) {
                log.error("Exception thrown during upload of the property zip file: " + e.getMessage(), e);
            }
            if (zipFile.exists()) {
                FileUtilities.forceDelete(zipFile);
            }
            return zipUrl;
        });
    }

    public String execute(File zipFile, String identifier) {
        CompletableFuture<String> zipFileFuture = uploadZip(zipFile, identifier);

        String result = null;
        try {
            result = zipFileFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }
}
