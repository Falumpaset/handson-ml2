package de.immomio.recipient.service.conversation;

import de.immomio.common.amazon.s3.S3FileManagerException;
import de.immomio.common.upload.FileStoreService;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.recipient.service.amazon.s3.RecipientS3FileManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.simplejavamail.api.email.AttachmentResource;
import org.simplejavamail.api.email.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static de.immomio.data.base.type.common.FileType.SHARED_DOCUMENT;

/**
 * @author Fabian Beck.
 */

@Slf4j
@Service
public class EmailAttachmentConverter {

    private final RecipientS3FileManager recipientS3FileManager;

    @Autowired
    public EmailAttachmentConverter(RecipientS3FileManager recipientS3FileManager) {
        this.recipientS3FileManager = recipientS3FileManager;
    }

    public List<S3File> saveAttachments(Email email) {
        return email.getAttachments()
                .stream()
                .map(this::saveAttachment)
                .filter((Objects::nonNull))
                .collect(Collectors.toList());
    }

    private S3File saveAttachment(AttachmentResource attachment) {
        try {
            String fileExtension = FilenameUtils.getExtension(attachment.getDataSource().getName());
            String fileName = FilenameUtils.getBaseName(attachment.getDataSource().getName());

            File folder = com.google.common.io.Files.createTempDir();
            File file = new File(folder, attachment.getName());
            Files.write(file.toPath(), attachment.readAllBytes());

            String identifier = FileStoreService.generateIdentifier();

            String s3FileUrl = recipientS3FileManager.uploadAttachment(file, SHARED_DOCUMENT, identifier,
                    fileExtension);

            S3File s3File = new S3File();
            s3File.setIdentifier(identifier);
            s3File.setType(SHARED_DOCUMENT);
            s3File.setUrl(s3FileUrl);
            s3File.setExtension(fileExtension);
            s3File.setTitle(fileName);
            s3File.setName(fileName);

            return s3File;
        } catch (IOException | S3FileManagerException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }
}
