package de.immomio.common.file;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

/**
 * @author Bastian Bliemeister
 */
@Slf4j
public final class DeleteOnCloseFileInputStream extends FileInputStream {

    private File file;

    private boolean includeDirIfEmpty;

    public DeleteOnCloseFileInputStream(File file) throws FileNotFoundException {
        this(file, false);
    }

    public DeleteOnCloseFileInputStream(File file, boolean includeDirIfEmpty) throws FileNotFoundException {
        super(file);

        this.file = file;
        this.includeDirIfEmpty = includeDirIfEmpty;
    }

    @Override
    public void close() {
        if (file == null || !file.exists()) {
            return;
        }

        File parent = null;
        if (includeDirIfEmpty) {
            parent = file.getParentFile();
        }

        log.info("Delete file on closing stream: " + file.getAbsolutePath());
        FileUtilities.forceDelete(file);

        if (parent != null && parent.isDirectory() && Objects.requireNonNull(parent.listFiles()).length == 0) {
            FileUtilities.forceDelete(parent);
        }
    }
}
