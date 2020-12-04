/**
 *
 */
package de.immomio.cloud.service.s3;

import de.immomio.common.amazon.s3.AbstractS3FileManager;
import de.immomio.common.amazon.s3.S3FileManagerException;
import de.immomio.common.upload.FileStoreService;
import de.immomio.data.base.type.common.FileType;
import de.immomio.imageResize.ImageUtilitiesException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Johannes Hiemer, Maik Kingma.
 */

@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public abstract class AbstractImageService<T extends AbstractS3FileManager> {

    private static final String DASH = "-";

    private List<Integer> sizes = new ArrayList<>();

    public AbstractImageService() {
        sizes.add(250);
        sizes.add(500);
        sizes.add(1000);
    }

    public abstract T getS3FileManager();

    public void resize(File file, FileType fileType, String identifier, String extension, boolean deleteFile) {

        tryResize(file, fileType, identifier, extension);

        if (deleteFile) {
            try {
                FileUtils.forceDelete(file);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public void customize(File file, FileType fileType, String identifier, String extension, double rotate,
                          boolean deleteFile) {

        File newFile;
        try {
            newFile = FileStoreService.rotateImage(file, rotate, extension);
            getS3FileManager().uploadImage(newFile, fileType, identifier, extension);
        } catch (ImageUtilitiesException e) {
            log.error("Error rotating Image.", e);
            return;
        } catch (S3FileManagerException e) {
            log.error("Error uploading Image.", e);
            return;
        }

        tryResize(newFile, fileType, identifier, extension);

        if (deleteFile) {
            try {
                FileUtils.forceDelete(file);
                FileUtils.forceDelete(newFile);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    public void resize(File file, FileType fileType, String identifier, String extension) {
        resize(file, fileType, identifier, extension, false);
    }

    public void deleteSizes(FileType fileType, String identifier, String extension) {
        for (Integer size : sizes) {
            try {
                getS3FileManager().deleteImage(fileType, generateFileName(identifier, size), extension);
            } catch (S3FileManagerException e) {
                log.error("Error deleting Imagesize.", e);
            }
        }
    }

    private void tryResize(File file, FileType fileType, String identifier, String extension) {
        for (Integer size : sizes) {
            try {
                File resizedImage = FileStoreService.resizeImage(file, size, extension, identifier);
                getS3FileManager().uploadImage(resizedImage, fileType, generateFileName(identifier, size), extension);

                boolean deleted = resizedImage.delete();
                if (!deleted) {
                    resizedImage.deleteOnExit();
                }
            } catch (S3FileManagerException | ImageUtilitiesException e) {
                log.error("Error resizing Image.", e);
            } catch (SecurityException e) {
                log.error("ERROR during deletion of temporary resized image file: " + e.getMessage()
                                  + ", CAUSE: " + e.getCause(), e);
            }
        }
    }

    private String generateFileName(String identifier, Integer size) {
        return identifier + DASH + size;
    }
}
