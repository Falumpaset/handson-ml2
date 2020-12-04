/**
 *
 */
package de.immomio.common.zip;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister.
 */
@Slf4j
@Service
public class FileZipper {

    private static void addFiles(File[] files, String path, ZipOutputStream zip) throws IOException {
        if (path == null) {
            path = "";
        } else if (!path.isEmpty() && !path.endsWith("/")) {
            path = path + "/"; // In einem Zip-File nicht BS abhängig -> File.separator
        }

        for (File fileOrFolder : files) {

            if (fileOrFolder.isDirectory()) {
                String folderPath = path + fileOrFolder.getName() + "/";

                log.info("Adding folder: " + folderPath);

                zip.putNextEntry(new ZipEntry(folderPath));
                zip.closeEntry();

                addFiles(fileOrFolder.listFiles(), folderPath, zip);

                continue;
            }

            String filePath = path + fileOrFolder.getName();

            log.info("Adding file: " + filePath);

            byte[] buf = new byte[1024];
            int len;

            zip.putNextEntry(new ZipEntry(filePath));

            try (FileInputStream fileInputStream = new FileInputStream(fileOrFolder)) {
                while ((len = fileInputStream.read(buf)) > 0) {
                    zip.write(buf, 0, len);
                }
            } catch (Exception ex) {
                log.error(ex.getMessage(), ex);
            }

            zip.closeEntry();
        }
    }

    public boolean zipFiles(String srcFolder, File destZipFile) {
        return zipFiles(srcFolder, destZipFile, true);
    }

    public boolean zipFiles(String srcFolder, File destZipFile, boolean includeDir) {
        if (srcFolder == null) {
            log.error("Error happened during the zip process -> srcFolder is null");

            return false;
        } else if (srcFolder.isEmpty()) {
            log.error("Error happened during the zip process -> srcFolder is an empty String");

            return false;
        }

        File folder;
        try {
            folder = new File(srcFolder);
        } catch (Exception e) {
            log.error("Some Errors happened during the zip process", e);

            return false;
        }

        return zipFiles(folder, destZipFile, includeDir);
    }

    public boolean zipFiles(File srcFolder, File destZipFile) {
        return zipFiles(srcFolder, destZipFile, true);
    }

    public boolean zipFiles(File srcFolder, File destZipFile, boolean includeDir) {
        if (srcFolder == null) {
            log.error("Error happened during the zip process -> srcFolder is null");
            return false;
        } else if (!srcFolder.isDirectory()) {
            log.error("Error happened during the zip process -> srcFolder is not a directory");
            return false;
        }

        try {
            zipFolder(srcFolder, destZipFile, includeDir);
        } catch (Exception e) {
            log.error("Some Errors happened during the zip process", e);
            return false;
        }
        return true;
    }

    private void zipFolder(File srcFolder, File destZipFile, boolean includeDir) throws Exception {
        ZipOutputStream zip;
        FileOutputStream fileWriter;

        fileWriter = new FileOutputStream(destZipFile);
        zip = new ZipOutputStream(fileWriter);

        String path = includeDir ? srcFolder.getName() : "";

        addFiles(srcFolder.listFiles(), path, zip);

        zip.flush();
        zip.close();
    }

    public void unzipFile(String zipFile, File outputFolder) {
        byte[] buffer = new byte[1024];

        try (ZipFile zFile = new ZipFile(zipFile)) {
            if (!outputFolder.exists()) {
                outputFolder.mkdir();
            }

            log.info("SIZE ZIP FILE" + zFile.size());
            Enumeration<? extends ZipEntry> entries = zFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File newFile = new File(outputFolder, entry.getName().toLowerCase());

                if (entry.isDirectory()) {
                    newFile.mkdir();
                } else {
                    newFile.getParentFile().mkdirs();

                    int len;
                    try (FileOutputStream fileOutputStream = new FileOutputStream(newFile)) {
                        InputStream inputStream = zFile.getInputStream(entry);
                        while ((len = inputStream.read(buffer)) > 0) {
                            fileOutputStream.write(buffer, 0, len);
                        }
                    } catch (Exception e) {
                        log.info("ERROR");
                        log.info("SIZE " + entry.getSize());
                        log.info("NAME" + entry.getName());
                        log.info("TIME" + entry.getTime());
                        log.info("CRC" + entry.getCrc());
                        log.info("COMMENT" + entry.getComment());
                        log.info(e.getMessage());
                    }
                }
            }
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        }
    }
}
