package de.immomio.common.upload;

import de.immomio.common.encryption.CryptoUtils;
import de.immomio.common.encryption.exception.CryptoException;
import de.immomio.data.base.type.common.FileType;
import de.immomio.imageResize.ImageUtilities;
import de.immomio.imageResize.ImageUtilitiesException;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static de.immomio.common.upload.FileConstants.APPLICATION_PDF;
import static de.immomio.common.upload.FileConstants.DASH;
import static de.immomio.common.upload.FileConstants.DOCX;
import static de.immomio.common.upload.FileConstants.DOCX_MIME;
import static de.immomio.common.upload.FileConstants.GIF;
import static de.immomio.common.upload.FileConstants.IMAGE_GIF;
import static de.immomio.common.upload.FileConstants.IMAGE_JPEG;
import static de.immomio.common.upload.FileConstants.IMAGE_JPG;
import static de.immomio.common.upload.FileConstants.IMAGE_PNG;
import static de.immomio.common.upload.FileConstants.JPG;
import static de.immomio.common.upload.FileConstants.MAX_DOT;
import static de.immomio.common.upload.FileConstants.PDF;
import static de.immomio.common.upload.FileConstants.PNG;

/**
 * @author Johannes Hiemer, Bastian Bliemeister, Maik Kingma
 */

public class FileStoreService {

    private static final String EMPTY_STRING = "";
    private static BufferedImage bufferedImage;

    public static FileStoreObject fileStoreObject(File file, FileType fileType, boolean encrypted) {
        FileStoreObject fileStoreObject = createFileStoreObject(fileType, encrypted);

        String fileExtension = multipartFileExtension(file);
        fileStoreObject.setExtension(fileExtension);
        fileStoreObject.setName(file.getName());

        return fileStoreObject;
    }

    public static FileStoreObject fileStoreObject(MultipartFile multipartFile, FileType fileType, boolean encrypted) {
        FileStoreObject fileStoreObject = createFileStoreObject(fileType, encrypted);

        String fileExtension = multipartFileExtension(multipartFile);
        fileStoreObject.setExtension(fileExtension);
        fileStoreObject.setName(multipartFile.getOriginalFilename());

        return fileStoreObject;
    }

    public static String generateIdentifier() {
        return UUID.randomUUID().toString().replace(DASH, EMPTY_STRING);
    }

    public static boolean isImage(File file) {
        return isImage(new MimetypesFileTypeMap().getContentType(file));
    }

    public static boolean isImage(MultipartFile file) {
        return isImage(file.getContentType());
    }

    private static FileStoreObject createFileStoreObject(FileType fileType, boolean encrypted) {
        FileStoreObject fileStoreObject = new FileStoreObject();
        fileStoreObject.setType(fileType);
        String identifier = generateIdentifier();
        fileStoreObject.setIdentifier(identifier);
        fileStoreObject.setEncrypted(encrypted);

        return fileStoreObject;
    }

    private static boolean isImage(String mimeType) {
        return mimeType.equals(IMAGE_GIF) || mimeType.equals(IMAGE_JPEG) || mimeType.equals(IMAGE_PNG);
    }

    public static boolean isDocument(File file) {
        return isDocument(new MimetypesFileTypeMap().getContentType(file));
    }

    public static boolean isDocument(MultipartFile file) {
        return isDocument(file.getContentType());
    }

    private static boolean isDocument(String mimetype) {
        return mimetype.equals(APPLICATION_PDF);
    }

    public static boolean validateFile(File file) {
        return validateMimetype(new MimetypesFileTypeMap().getContentType(file));
    }

    public static boolean validateMultipartFile(MultipartFile file) {
        return validateMimetype(file.getContentType());
    }

    private static boolean validateMimetype(String mimetype) {
        return multipartFileExtension(mimetype) != null;
    }

    private static String multipartFileExtension(File file) {
        return multipartFileExtension(new MimetypesFileTypeMap().getContentType(file));
    }

    private static String multipartFileExtension(MultipartFile file) {
        return multipartFileExtension(file.getContentType());
    }

    public static String multipartFileExtension(String mimetype) {
        if (mimetype == null || mimetype.isEmpty()) {
            return null;
        } else if (mimetype.equals(IMAGE_GIF)) {
            return GIF;
        } else if (mimetype.equals(IMAGE_JPEG)) {
            return JPG;
        } else if (mimetype.equals(IMAGE_JPG)) {
            return JPG;
        } else if (mimetype.equals(IMAGE_PNG)) {
            return PNG;
        } else if (mimetype.equals(APPLICATION_PDF)) {
            return PDF;
        } else if (mimetype.equals(DOCX_MIME)) {
            return DOCX;
        }

        return null;
    }

    public static File resizeImage(File original, int size, String extension, String identifier) throws ImageUtilitiesException {
        File outputFile = new File(identifier + DASH + size + MAX_DOT + extension);
        try {
            bufferedImage = ImageIO.read(original);
            Image image = ImageUtilities.scaleImage(bufferedImage, size, ImageUtilities.Limit.MAX);
            bufferedImage = ImageUtilities.toBufferedImage(image);

            ImageIO.write(ImageUtilities.toBufferedImage(image), extension, outputFile);
        } catch (Exception ex) {
            throw new ImageUtilitiesException(ex);
        }
        return outputFile;
    }

    public static File rotateImage(File original, double degrees, String extension) throws ImageUtilitiesException {
        File outputFile = new File(original.getName());
        try {
            bufferedImage = ImageIO.read(original);
            bufferedImage = ImageUtilities.rotateImage(bufferedImage, degrees);

            ImageIO.write(bufferedImage, extension, outputFile);
        } catch (IOException ex) {
            throw new ImageUtilitiesException(ex);
        }

        return outputFile;
    }

    public static File encryptFile(File inputFile, String key) throws CryptoException {
        key = key.substring(0, 16);
        return CryptoUtils.encrypt(key, inputFile);
    }

    public static File decryptFile(File inputFile, String key) throws CryptoException {
        key = key.substring(0, 16);
        return CryptoUtils.decrypt(key, inputFile);
    }

}
