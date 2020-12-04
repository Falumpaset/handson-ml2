package de.immomio.mail.sender;

import de.immomio.common.amazon.s3.S3FileManagerException;
import de.immomio.common.zip.FileZipper;
import de.immomio.data.base.type.common.FileType;
import de.immomio.mail.s3.MailLandlordS3FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class LandlordAttachmentHandler extends AbstractAttachmentHandler{

    private final MailLandlordS3FileManager mailLandlordS3FileManager;

    @Autowired
    public LandlordAttachmentHandler(MailLandlordS3FileManager mailLandlordS3FileManager, FileZipper fileZipper) {
        super(fileZipper);
        this.mailLandlordS3FileManager = mailLandlordS3FileManager;
    }

    @Override
    public String uploadMailFile(File file, FileType fileType, String identifier, String extension)
            throws S3FileManagerException {
        return mailLandlordS3FileManager.uploadFile(
                file,
                fileType,
                identifier,
                extension,
                mailLandlordS3FileManager.getBUCKET_MAIL());
    }
}
