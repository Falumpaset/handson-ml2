/**
 *
 */
package de.immomio.common.zip;

import de.immomio.common.CommonToolsConfiguration;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;

/**
 * @author Johannes Hiemer.
 */
@ActiveProfiles(value = {"test-api", "development"})
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        CommonToolsConfiguration.class
})
public class FileZipperTest {

    File sourceFolder = new File(new File(".").getAbsolutePath() + "/src/test/resources/zipper");

    @Autowired
    private FileZipper fileZipper;

    @Test
    public void test() throws IOException {

        File destinationFile = new File("sample.zip");

        fileZipper.zipFiles(sourceFolder, destinationFile, true);

        FileUtils.deleteQuietly(destinationFile);
    }

}
