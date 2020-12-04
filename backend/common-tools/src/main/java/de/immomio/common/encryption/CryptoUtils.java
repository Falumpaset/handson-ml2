/**
 *
 */
package de.immomio.common.encryption;

import biweekly.util.org.apache.commons.codec.binary.Base64;
import de.immomio.common.encryption.exception.CryptoException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * @author Johannes Hiemer.
 */
@Service
public class CryptoUtils {

    private static final String UNICODE_FORMAT = "UTF8";

    private static final String ALGORITHM = "AES";

    private static final String TRANSFORMATION = "AES";

    public static File encrypt(String key, File inputFile)
            throws CryptoException {
        return encryption(Cipher.ENCRYPT_MODE, key, inputFile);
    }

    public static File decrypt(String key, File inputFile) throws CryptoException {
        return encryption(Cipher.DECRYPT_MODE, key, inputFile);
    }

    private static File encryption(int cipherMode, String key, File inputFile) throws CryptoException {
        File outputFile;
        try {
            String prefix = FilenameUtils.getBaseName(inputFile.getName());
            String suffix = "." + FilenameUtils.getExtension(inputFile.getName());
            outputFile = File.createTempFile(prefix, suffix);
        } catch (IOException ex) {
            throw new CryptoException("Error requesting the filename prefix/suffix", ex);
        }

        try (FileInputStream inputStream = new FileInputStream(inputFile);
             FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(cipherMode, secretKey);

            byte[] inputBytes = new byte[(int) inputFile.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            outputStream.write(outputBytes);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | IOException ex) {
            throw new CryptoException("Error encrypting/decrypting file", ex);
        }

        return outputFile;
    }

    public static String encryptString(String key, String input)
            throws CryptoException {
        String encrypted;
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] plainText = input.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encrypted = new String(Base64.encodeBase64(encryptedText));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException | UnsupportedEncodingException ex) {
            throw new CryptoException("Error encrypting/decrypting string", ex);
        }

        return encrypted;
    }

    public static String decryptString(String key, String input)
            throws CryptoException {
        String decrypted;
        try {
            Key secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] encryptedText = Base64.decodeBase64(input);
            byte[] plainText = cipher.doFinal(encryptedText);

            decrypted = new String(plainText);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException
                | InvalidKeyException | BadPaddingException
                | IllegalBlockSizeException ex) {
            throw new CryptoException("Error encrypting/decrypting string", ex);
        }

        return decrypted;
    }

}
