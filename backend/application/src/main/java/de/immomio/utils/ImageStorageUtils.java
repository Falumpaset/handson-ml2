package de.immomio.utils;

import com.google.common.io.Files;
import de.immomio.cloud.service.s3.AbstractImageService;
import de.immomio.common.amazon.s3.AbstractS3FileManager;
import de.immomio.common.amazon.s3.S3FileManagerException;
import de.immomio.common.encryption.exception.CryptoException;
import de.immomio.common.upload.FileStoreObject;
import de.immomio.common.upload.FileStoreService;
import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.ImmomioRuntimeException;
import de.immomio.data.base.type.common.FileType;
import de.immomio.data.shared.bean.common.S3File;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

@Slf4j
public class ImageStorageUtils {


    private static final String MULTIPART_NOT_VALID_L = "MULTIPART_NOT_VALID_L";

    public static ResponseEntity uploadImage(MultipartFile multipartFile,
            AbstractS3FileManager s3FileManager,
            AbstractImageService imageService, Double rotate) {

        FileType fileType = FileType.IMG;
        File folder = Files.createTempDir();
        try {
            if (multipartFile.isEmpty() || !FileStoreService.validateMultipartFile(multipartFile)) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            FileStoreObject fileStoreObject = FileStoreService.fileStoreObject(multipartFile, fileType, false);
            if (fileStoreObject == null || !StringUtils.isNotEmpty(fileStoreObject.getIdentifier())) {
                log.info("Error uploading, no valid file format");

                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String extension = fileStoreObject.getExtension();
            File file = s3FileManager.createTempFile(multipartFile, fileType, fileStoreObject.getIdentifier(),
                    extension, folder);

            imageService.customize(file, fileType, fileStoreObject.getIdentifier(), extension,
                    rotate == null ? 0 : rotate, true);

            String url = s3FileManager.getImageUrl(fileType, fileStoreObject.getIdentifier(), extension);

            fileStoreObject.setUrl(url);
            try {
                FileUtils.deleteQuietly(file);
                FileUtils.forceDelete(folder);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            return new ResponseEntity<>(fileStoreObject, HttpStatus.CREATED);
        } catch (S3FileManagerException | MalformedURLException e) {
            log.info("Error uploading image", e);

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static FileStoreObject uploadFile(MultipartFile multipartFile,
            FileType fileType,
            boolean encrypted,
            String encryptionKey,
            AbstractS3FileManager s3FileManager,
            AbstractImageService imageService) {

        if (multipartFile.isEmpty() || !FileStoreService.validateMultipartFile(multipartFile)) {
            throw new ApiValidationException(MULTIPART_NOT_VALID_L);
        }

        FileStoreObject fileStoreObject = FileStoreService.fileStoreObject(multipartFile, fileType, encrypted);
        if (fileStoreObject == null || !StringUtils.isNotEmpty(fileStoreObject.getIdentifier())) {
            log.info("Error uploading, no valid file format");
            throw new ImmomioRuntimeException();
        }

        File folder = Files.createTempDir();
        File file = createFile(multipartFile, fileStoreObject, folder, s3FileManager);

        String url = uploadFile(file, fileStoreObject, encryptionKey, s3FileManager, imageService);
        fileStoreObject.setUrl(url);

        try {
            FileUtils.forceDelete(file);
            FileUtils.forceDelete(folder);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return fileStoreObject;
    }

    public static S3File convertFileStoreObject(FileStoreObject fileStoreObject) {
        S3File s3File = new S3File();

        s3File.setType(fileStoreObject.getType());
        s3File.setUrl(fileStoreObject.getUrl());
        s3File.setIdentifier(fileStoreObject.getIdentifier());
        s3File.setTitle(fileStoreObject.getName());
        s3File.setName(fileStoreObject.getName());
        s3File.setExtension(fileStoreObject.getExtension());

        return s3File;
    }

    private static File createFile(MultipartFile multipartFile, FileStoreObject fileStoreObject, File folder, AbstractS3FileManager s3FileManager) {
        try {
            return s3FileManager.createTempFile(multipartFile, fileStoreObject.getType(),
                    fileStoreObject.getIdentifier(), fileStoreObject.getExtension(), folder);
        } catch (S3FileManagerException e) {
            log.error("Error creating file", e);
            throw new ImmomioRuntimeException();
        }
    }

    private static String uploadFile(
            File file,
            FileStoreObject fileStoreObject,
            String encryptionKey,
            AbstractS3FileManager s3FileManager,
            AbstractImageService imageService) {

        FileType fileType = fileStoreObject.getType();

        try {
            String fileIdentifier = fileStoreObject.getIdentifier();
            switch (fileType) {
                case IMG:
                    imageService.resize(file, fileType, fileIdentifier,
                            fileStoreObject.getExtension());
                    return s3FileManager.uploadImage(file, fileType, fileIdentifier,
                            fileStoreObject.getExtension());
                default:
                    if (fileStoreObject.isEncrypted()) {
                        File tmp = FileStoreService.encryptFile(file, encryptionKey);
                        fileStoreObject.setEncrypted(true);

                        FileUtils.deleteQuietly(file);
                        file = tmp;
                    }

                    return s3FileManager.uploadFile(file, fileType, fileIdentifier,
                            fileStoreObject.getExtension());
            }
        } catch (S3FileManagerException | CryptoException e) {
            log.error("Error uploading file", e);
            throw new ImmomioRuntimeException();
        }
    }

}
