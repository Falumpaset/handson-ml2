package de.immomio.imageResize;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * File folder 		= new File("filepath"); BufferedImage bi 	= ImageIO.read(file);
 * <p>
 * // OPTIONAL wenn Infos zum Image gebraucht werden ImageInfo imageInfo = new ImageInfo(); imageInfo.setInput(new
 * FileInputStream(file)); imageInfo.check();
 * <p>
 * <p>
 * // Die kürzere Seite des konvertierten Bildes ist 180px lang. image = ImageUtilities.scaleImage(bi, 180,
 * ImageUtilities.Limit.MIN); ImageIO.write(ImageUtilities.toBufferedImage(image), "jpg", "_180MIN.jpg"));
 * <p>
 * // Die längere Seite des konvertierten Bildes ist 250px lang. image = ImageUtilities.scaleImage(bi, 250,
 * ImageUtilities.Limit.MAX); ImageIO.write(ImageUtilities.toBufferedImage(image), "jpg", "_250MAX.jpg"));
 * <p>
 * // Das konvertierte Bild hat eine maximale Größe von 1920x1080 Pixel image = ImageUtilities.scaleImage(bi, 1920,
 * 1080, false); ImageIO.write(ImageUtilities.toBufferedImage(image), "jpg", "_FULL.jpg"));
 *
 * @author Bastian Bliemeister
 */
public abstract class ImageUtilities {

    private ImageUtilities() {
    }

    public static BufferedImage rotateImage(BufferedImage source, double degrees) {

        return rotateImage(source, degrees, BufferedImage.TYPE_INT_RGB);
    }

    public static BufferedImage rotateImage(BufferedImage source, double degrees, int type) {
        if (source == null) {
            throw new IllegalArgumentException("source is null");
        } else if (degrees < 0) {
            throw new IllegalArgumentException("degrees is < 0");
        } else if (degrees > 360) {
            throw new IllegalArgumentException("degrees is > 360");
        } else if (degrees == 0) {
            return source;
        }

        double theta = Math.toRadians(degrees);

        AffineTransform xform = new AffineTransform();

        int w, h;
        if (degrees >= 45 && degrees <= 135 || degrees >= 225 && degrees <= 315) {
            w = source.getHeight();
            h = source.getWidth();

            xform.translate(0.5 * w, 0.5 * h);
            xform.rotate(theta);
            xform.translate(-0.5 * h, -0.5 * w);
        } else {
            w = source.getWidth();
            h = source.getHeight();

            xform.translate(0.5 * w, 0.5 * h);
            xform.rotate(theta);
            xform.translate(-0.5 * w, -0.5 * h);
        }

        BufferedImage ret = new BufferedImage(w, h, type);

        Graphics2D g2d = ret.createGraphics();

        g2d.drawImage(source, xform, null);
        g2d.dispose();

        return ret;
    }

    public static Image scaleImage(BufferedImage source, int newX, int newY,
                                   boolean upscaling) {
        return scaleImage(source, newX, newY, upscaling, 0);
    }

    public static Image scaleImage(BufferedImage source, int newX, int newY,
                                   boolean upscaling, double rotate) {
        if (source == null) {
            throw new IllegalArgumentException("source is null");
        }

        source = rotateImage(source, rotate);

        int oldX = source.getWidth();
        int oldY = source.getHeight();

        double scale = calculateScale(oldX, oldY, newX, newY);

        if (!upscaling && scale > 1) {
            scale = 1;
        }

        return scaleImage(source, scale);
    }

    public static Image scaleImage(BufferedImage source, double scale) {
        return scaleImage(source, scale, Image.SCALE_SMOOTH);
    }

    public static Image scaleImage(BufferedImage source, double scale, double rotate) {
        return scaleImage(source, scale, Image.SCALE_SMOOTH, rotate);
    }

    public static Image scaleImage(BufferedImage source, double scale, int hint) {
        return scaleImage(source, scale, hint, 0);
    }

    public static Image scaleImage(BufferedImage source, double scale, int hint, double rotate) {
        if (source == null) {
            throw new IllegalArgumentException("source is null");
        } else if (scale <= 0) {
            throw new IllegalArgumentException("scale is <= 0");
        }

        source = rotateImage(source, rotate);

        int width = (int) (source.getWidth() * scale);
        int height = (int) (source.getHeight() * scale);

        return source.getScaledInstance(width, height, hint);
    }

    public static Image scaleImage(BufferedImage source, int size, Limit limit) throws IllegalArgumentException{
        return scaleImage(source, size, limit, 0);
    }

    private static Image scaleImage(BufferedImage source, int size, Limit limit, double rotate) throws IllegalArgumentException{
        if (source == null) {
            throw new IllegalArgumentException("source is null");
        } else if (limit == null) {
            throw new IllegalArgumentException("limit is null");
        } else if (size <= 0) {
            throw new IllegalArgumentException("size is <= 0");
        }

        source = rotateImage(source, rotate);

        int oldX = source.getWidth();
        int oldY = source.getHeight();

        double scale;

        switch (limit) {
            case MAX:
                scale = oldX > oldY ? calculateScale(oldX, oldY, size, oldY) : calculateScale(oldX, oldY, oldX, size);
                break;
            case MIN:
            default:
                scale = oldX < oldY ? calculateScale(oldX, oldY, size, oldY) : calculateScale(oldX, oldY, oldX, size);
                break;
        }

        return scaleImage(source, scale);
    }

    public static BufferedImage toBufferedImage(Image image) {
        return toBufferedImage(image, BufferedImage.TYPE_INT_RGB);
    }

    public static BufferedImage toBufferedImage(Image image, int type) {
        if (image == null) {
            throw new IllegalArgumentException("image is null");
        } else if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        int width = image.getWidth(null);
        int height = image.getHeight(null);

        BufferedImage ret = new BufferedImage(width, height, type);

        Graphics2D g = ret.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return ret;
    }

    public static boolean checkFile(File file) {
        if (file == null) {
            return false;
        }

        BufferedImage img;
        try {
            img = ImageIO.read(file);
        } catch (IOException e) {
            return false;
        }

        return img != null;
    }

    public static double calculateScale(int oldX, int oldY, int newX, int newY) {
        if (oldX <= 0) {
            throw new IllegalArgumentException("old x-value <= 0");
        } else if (oldY <= 0) {
            throw new IllegalArgumentException("old y-value <= 0");
        } else if (newX <= 0) {
            throw new IllegalArgumentException("new x-value <= 0");
        } else if (newY <= 0) {
            throw new IllegalArgumentException("new y-value <= 0");
        }

        double scaleX = (double) newX / (double) oldX;
        double scaleY = (double) newY / (double) oldY;

        return scaleX < scaleY ? scaleX : scaleY;
    }

    @SuppressWarnings("unused")
    private static int evaluateType(BufferedImage image) {
        return BufferedImage.TYPE_INT_RGB;
    }

    public enum Limit {
        MIN, MAX
    }
}
