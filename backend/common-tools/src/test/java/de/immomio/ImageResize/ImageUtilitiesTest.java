package de.immomio.ImageResize;

import com.google.common.io.Files;
import de.immomio.imageResize.ImageInfo;
import de.immomio.imageResize.ImageUtilities;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

/**
 * @author Bastian Bliemeister
 */
@RunWith(SpringJUnit4ClassRunner.class)
public class ImageUtilitiesTest {

    @Test
    public void test() throws IOException {
        File testFile = new File(new File(".").getAbsolutePath() + "/src/test/resources/imageResize.jpg");
        BufferedImage bi = ImageIO.read(testFile);

        File tmpDir = Files.createTempDir();

        try {
            ImageInfo imageInfo = new ImageInfo();
            Image image;

            File file;

            // Die kürzere Seite des konvertierten Bildes ist 180px lang.
            file = new File(tmpDir, "1_" + testFile.getName());
            image = ImageUtilities.scaleImage(bi, 180, ImageUtilities.Limit.MIN);

            ImageIO.write(ImageUtilities.toBufferedImage(image), "jpg", file);

            imageInfo.setInput(new FileInputStream(file));

            assertTrue(imageInfo.check());
            assertTrue((imageInfo.getWidth() >= imageInfo.getHeight() && near(imageInfo.getHeight(), 180)) ||
                    (imageInfo.getHeight() >= imageInfo.getWidth() && near(imageInfo.getWidth(), 180)));

            // Die längere Seite des konvertierten Bildes ist 250px lang.
            file = new File(tmpDir, "2_" + testFile.getName());
            image = ImageUtilities.scaleImage(bi, 250, ImageUtilities.Limit.MAX);

            ImageIO.write(ImageUtilities.toBufferedImage(image), "jpg", file);

            imageInfo.setInput(new FileInputStream(file));

            assertTrue(imageInfo.check());
            assertTrue((imageInfo.getWidth() >= imageInfo.getHeight() && near(imageInfo.getWidth(), 250)) ||
                    (imageInfo.getHeight() >= imageInfo.getWidth() && near(imageInfo.getHeight(), 250)));

            // Das konvertierte Bild hat eine maximale Größe von 1920x1080 Pixel
            file = new File(tmpDir, "3_" + testFile.getName());
            image = ImageUtilities.scaleImage(bi, 1920, 1080, false);

            ImageIO.write(ImageUtilities.toBufferedImage(image), "jpg", file);

            imageInfo.setInput(new FileInputStream(file));

            assertTrue(imageInfo.check());
            assertTrue(((imageInfo.getWidth() >= imageInfo.getHeight() && imageInfo.getWidth() <= 1920) ||
                    (imageInfo.getHeight() >= imageInfo.getWidth() && imageInfo.getHeight() <= 1920)) &&
                    ((imageInfo.getWidth() <= imageInfo.getHeight() && imageInfo.getWidth() <= 1080) ||
                            (imageInfo.getHeight() <= imageInfo.getWidth() && imageInfo.getHeight() <= 1080)));
        } finally {
            tmpDir.delete();
        }
    }

    private boolean near(int value, int goal) {
        double max = goal + value / 100;
        double min = goal - value / 100;

        return goal <= max && goal >= min;
    }
}
