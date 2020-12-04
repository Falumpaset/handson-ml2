package de.immomio.mail.sender;

import com.google.common.io.Files;
import de.immomio.common.amazon.s3.S3FileManagerException;
import de.immomio.common.file.FileUtilities;
import de.immomio.common.upload.FileStoreService;
import de.immomio.common.zip.FileZipper;
import de.immomio.data.base.type.common.FileType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public abstract class AbstractAttachmentHandler {

    private static final String S3FILE_KEY = "s3file";
    private static final String DOTZIP = ".zip";
    private static final String ZIP = "zip";

    private final FileZipper fileZipper;

    public AbstractAttachmentHandler(FileZipper fileZipper) {
        this.fileZipper = fileZipper;
    }

    public Map<String, String> uploadAttachments(Map<String, String> attachments) {
        if (attachments == null || attachments.size() == 0) {
            return null;
        }

        File destinationFolder = Files.createTempDir();
        Map<String, String> tmp = new HashMap<>();

        for (Map.Entry<String, String> entry : attachments.entrySet()) {
            log.info("Attachment File " + entry.getValue());
            File source = new File(entry.getValue());

            if (!source.exists()) {
                log.error("Attachment does not exists -> " + entry.toString());
                continue;
            } else if (source.isDirectory()) {
                log.error("Attachment is a directory -> " + entry.toString());
                continue;
            }

            File target = new File(destinationFolder, source.getName());

            try {
                Files.copy(source, target);
                FileUtilities.forceDelete(source);
            } catch (IOException e) {
                log.error("Error copying file ...", e);
            }
        }

        File zipFolder = Files.createTempDir();
        File zipFile = new File(zipFolder, UUID.randomUUID().toString() + DOTZIP);
        fileZipper.zipFiles(destinationFolder, zipFile, false);

        String identifier = FileStoreService.generateIdentifier();
        String zipUrl;
        try {
            zipUrl = uploadMailFile(zipFile, FileType.MAILATTACHMENTS, identifier, ZIP);
        } catch (S3FileManagerException e) {
            // add one more retry for testing
            log.error("Error uploading attachments ...", e);

            zipUrl = null;
        }

        FileUtils.deleteQuietly(destinationFolder);
        FileUtils.deleteQuietly(zipFolder);

        if (zipUrl == null) {
            return null;
        }

        tmp.put(S3FILE_KEY, zipUrl);

        return tmp;
    }

    public abstract String uploadMailFile(File file, FileType fileType, String identifier, String extension)
            throws S3FileManagerException;
}
