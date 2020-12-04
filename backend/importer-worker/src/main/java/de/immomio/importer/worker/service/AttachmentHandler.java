package de.immomio.importer.worker.service;

import de.immomio.cloud.service.s3.AbstractImageService;
import de.immomio.common.amazon.s3.S3FileManagerException;
import de.immomio.common.upload.FileStoreService;
import de.immomio.constants.exceptions.FileNotReadableException;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.shared.bean.common.S3File;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Maik Kingma
 */

@Slf4j
@Service
public class AttachmentHandler {

    private final WorkerFileService workerFileService;

    private final AbstractImageService imageResizeService;

    @Autowired
    public AttachmentHandler(WorkerFileService workerFileService, AbstractImageService imageResizeService) {
        this.workerFileService = workerFileService;
        this.imageResizeService = imageResizeService;
    }

    void handleAttachmentZipFromImport(File downloadedFile, Property property) {
        try {
            File unzipFile = workerFileService.unzip(downloadedFile);

            try {
                Files.walk(Paths.get(unzipFile.getPath()))
                        .filter(Files::isRegularFile)
                        .forEach(file -> {
                            log.info(file.getFileName() + ":");
                            log.info(file.toString());
                        });
            } catch (IOException e) {
                log.error("COULD NOT LOG UNZIPPED FOLDER CONTENT");
            }

            try {
                List<S3File> propertyAttachments = property.getData().getAttachments();
                List<S3File> toBeDeleted = new ArrayList<>();
                propertyAttachments.stream()
                        .filter(Objects::nonNull)
                        .forEach(attachment -> {
                            String path = unzipFile + File.separator + attachment.getUrl();

                            log.info("Trying to load attachment -> " + path);

                            attachment.setIdentifier(FileStoreService.generateIdentifier());

                            String url;
                            try {
                                File file = new File(path);

                                log.info("starting resize for image " + file.getPath());

                                if (!file.canRead()) {
                                    log.info("trying to set read right to true");
                                    boolean updateReadRightSuccess = file.setReadable(true);
                                    if (!updateReadRightSuccess && !file.canRead()) {
                                        log.error("File " + file.getPath() + " cannot be read!");
                                        throw new FileNotReadableException("File " + file.getPath() + " cannot be read!");
                                    }
                                }

                                imageResizeService.resize(file, attachment.getType(), attachment.getIdentifier(),
                                        attachment.getExtension());

                                url = workerFileService.uploadImage(file, attachment);
                                attachment.setUrl(url);
                            } catch (Exception e) {
                                toBeDeleted.add(attachment);

                                log.error("Error importing attachment: " + e.getMessage(), e);
                            }
                        });

                propertyAttachments.removeAll(toBeDeleted);

                toBeDeleted.clear();
                List<S3File> propertyDocuments = property.getData().getDocuments();
                for (S3File attachment : propertyDocuments) {
                    String fileName = attachment.getUrl();
                    String path = unzipFile + File.separator + fileName;

                    log.info("Trying to load document -> " + path);

                    attachment.setIdentifier(FileStoreService.generateIdentifier());

                    String url;
                    try {
                        File file = new File(path);

                        url = workerFileService.uploadDocument(file, attachment);
                        attachment.setUrl(url);
                    } catch (S3FileManagerException e) {
                        toBeDeleted.add(attachment);

                        log.error("Error importing attachment", e);
                    }
                }
                propertyDocuments.removeAll(toBeDeleted);
            } finally {
                FileUtils.deleteQuietly(unzipFile);
            }
        } finally {
            FileUtils.deleteQuietly(downloadedFile.getParentFile());
        }
    }
}
