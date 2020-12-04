/**
 *
 */
package de.immomio.common.encryption;

import de.immomio.common.CommonToolsConfiguration;
import de.immomio.common.encryption.exception.CryptoException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;

/**
 * @author Johannes Hiemer.
 */
@ActiveProfiles(value = {"test-api", "development"})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        CommonToolsConfiguration.class
})
public class EncryptionUtilsTest {

    private String key = "3md93asaidio9sw9";

    @Test
    public void testFileEncryption() throws CryptoException {
        String sourceFilePath = this.getClass().getResource("/littertraycat.jpg").getFile();

        File encryptedFile = CryptoUtils.encrypt(key, new File(sourceFilePath));

        Assert.assertNotNull(encryptedFile);

        File decryptedFile = CryptoUtils.decrypt(key, encryptedFile);

        Assert.assertNotNull(decryptedFile);
    }

    @Test
    public void testLargerFileEncryption() throws CryptoException {
        String sourceFilePath = this.getClass().getResource("/document.pdf").getFile();

        File encryptedFile = CryptoUtils.encrypt(key, new File(sourceFilePath));

        Assert.assertNotNull(encryptedFile);

        File decryptedFile = CryptoUtils.decrypt(key, encryptedFile);

        Assert.assertNotNull(decryptedFile);
    }

    @Test
    public void testStringEncryption() throws CryptoException {
        String sampleString = "Immomio wird in Zukunft die Portale renovieren";

        String encryptedString = CryptoUtils.encryptString(key, sampleString);

        Assert.assertNotNull(encryptedString);

        String decryptedString = CryptoUtils.decryptString(key, encryptedString);

        Assert.assertTrue(sampleString.equals(decryptedString));
    }

}
