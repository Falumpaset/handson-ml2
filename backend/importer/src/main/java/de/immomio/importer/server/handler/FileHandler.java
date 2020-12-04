package de.immomio.importer.server.handler;

import de.immomio.common.zip.FileZipper;
import de.immomio.data.base.type.common.FileType;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.importer.ImportMailData;
import de.immomio.data.landlord.entity.property.importer.ImportObject;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.importer.server.handler.util.AttachmentsFilter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
public class FileHandler {

    @Getter
    private final FileZipper fileZipper;

    @Autowired
    public FileHandler(FileZipper fileZipper) {
        this.fileZipper = fileZipper;
    }

    ImportObject createImportObject(
            Property property,
            Long customerId,
            ImportMailData mailData,
            Long importLogId,
            Boolean wbs
    ) {
        ImportObject importObject = new ImportObject();
        importObject.setProperty(property);
        importObject.setCustomerId(customerId);
        importObject.setMailData(mailData);
        importObject.setImportLogId(importLogId);
        importObject.setWbs(wbs);
        return importObject;
    }

    S3File createZippedAttachments(String identifier, String zipUrl) {
        S3File zippedAttachments = new S3File();
        zippedAttachments.setExtension("zip");
        zippedAttachments.setEncrypted(false);
        zippedAttachments.setIdentifier(identifier);
        zippedAttachments.setType(FileType.ZIP);
        zippedAttachments.setUrl(zipUrl);

        return zippedAttachments;
    }

    Set<File> getAttachmentsFromFolder(File folder) {
        return new AttachmentsFilter().getAttachmentsFromFolder(folder);
    }

    List<S3File> updateAndCopyAttachment(List<S3File> s3Files, File destinationFolder) {
        s3Files.forEach(attachment -> {
            try {
                File tmpFile = getFileAndCopyToDestination(attachment, destinationFolder);
                attachment.setUrl(tmpFile.getName());
            } catch (Exception e) {
                log.error("Skip attachment -> " + attachment.getUrl(), e);
            }
        });
        return s3Files;
    }

    private File getFileAndCopyToDestination(String uri, File destinationFolder) {
        return getFileAndCopyToDestination(uri, destinationFolder, null);
    }

    File getFileAndCopyToDestination(String uri, File destinationFolder, File sourceFolder) {
        if (uri == null || uri.isEmpty()) {
            return null;
        }

        File file;
        if (uri.startsWith("file://")) {
            file = getExternalFile(uri, sourceFolder);
        } else {
            try {
                file = getRemoteFile(uri, destinationFolder);
            } catch (Exception e) {
                return null;
            }
        }

        if (file != null) {
            copyFileToDirectory(file, destinationFolder);
        }

        return file;
    }

    private void copyFileToDirectory(File file, File destinationFolder) {
        try {
            FileUtils.copyFileToDirectory(file, destinationFolder);
        } catch (IOException e) {
            log.error("Error adding attachment -> ", e);
        }

    }

    private File getExternalFile(String uri) {
        return getExternalFile(uri, null);
    }

    private File getExternalFile(String uri, File sourceFolder) {
        String fileName = uri.replace("file://", "");

        File file;
        if (sourceFolder == null) {
            file = new File(fileName);
        } else {
            file = new File(sourceFolder, fileName);
        }

        if (!file.exists()) {
            return null;
        }

        return file;
    }

    private File getRemoteFile(String uri, File destinationFolder) throws IOException {
        String filename = UUID.randomUUID().toString() + "_" + FilenameUtils.getName(uri);
        File file = new File(destinationFolder, filename);
        URL url = new URL(uri);

        FileUtils.copyURLToFile(url, file);

        return file;
    }

    private File getFileAndCopyToDestination(S3File attachment, File destinationFolder) {
        String url = "file://" + attachment.getUrl();

        return getFileAndCopyToDestination(url, destinationFolder);
    }
}
