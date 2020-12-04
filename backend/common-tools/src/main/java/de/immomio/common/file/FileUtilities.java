package de.immomio.common.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

@Slf4j
public abstract class FileUtilities {

    private FileUtilities() {
    }

    public static String detectImageMimetype(File file) {
        return detectMimetype(file);
    }

    public static String detectMimetype(File file) {

        String mimeType = null;
        try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
            mimeType = URLConnection.guessContentTypeFromStream(is);
        } catch (IOException ignored) {
            log.warn(ignored.getMessage(), ignored);
        }

        return mimeType;
    }

    public static void forceDelete(File file) {
        try {
            FileUtils.forceDelete(file);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
