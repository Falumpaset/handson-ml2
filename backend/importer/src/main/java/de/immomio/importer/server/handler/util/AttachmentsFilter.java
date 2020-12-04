package de.immomio.importer.server.handler.util;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class AttachmentsFilter {

    private final String[] EXTENSIONS = new String[]{"jpg", "JPG"};

    private Set<File> attachments = new HashSet<>();

     public Set<File> getAttachmentsFromFolder(File folder) {

        setAttachments(folder);

        return attachments;
    }

    private void setAttachments(File folder) {

        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {

                    if (FilenameUtils.isExtension(file.getAbsolutePath(), EXTENSIONS)) {

                        File renamed = new File(file.getParent() + "/" + file.getName().toLowerCase());
                        file.renameTo(renamed);

                        attachments.add(renamed);

                    } else if (file.isDirectory()) {
                        setAttachments(file);
                    }
                }
            }
        }
    }
}
