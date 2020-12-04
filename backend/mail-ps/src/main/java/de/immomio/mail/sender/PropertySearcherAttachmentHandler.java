package de.immomio.mail.sender;

import de.immomio.common.amazon.s3.S3FileManagerException;
import de.immomio.common.zip.FileZipper;
import de.immomio.data.base.type.common.FileType;
import de.immomio.mail.s3.MailPropertySearcherS3FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class PropertySearcherAttachmentHandler extends AbstractAttachmentHandler {

    private final MailPropertySearcherS3FileManager mailPropertySearcherS3FileManager;

    @Autowired
    public PropertySearcherAttachmentHandler(MailPropertySearcherS3FileManager mailPropertySearcherS3FileManager, FileZipper fileZipper) {
        super(fileZipper);
        this.mailPropertySearcherS3FileManager = mailPropertySearcherS3FileManager;
    }

    public String uploadMailFile(File file, FileType fileType, String identifier, String extension) throws S3FileManagerException {
        return mailPropertySearcherS3FileManager.uploadFile(file, fileType, identifier, extension, mailPropertySearcherS3FileManager.getBUCKET_MAIL());
    }
}
