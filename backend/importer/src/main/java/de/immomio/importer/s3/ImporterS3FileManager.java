package de.immomio.importer.s3;

import de.immomio.common.amazon.s3.AbstractS3ImporterFileManager;
import de.immomio.common.amazon.s3.S3FileManagerException;
import de.immomio.data.base.type.common.FileType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;

@Slf4j
@Service
public class ImporterS3FileManager extends AbstractS3ImporterFileManager<ImporterS3> {

    private ImporterS3 importerS3;

    @Autowired
    public ImporterS3FileManager(ImporterS3 importerS3) {
        this.importerS3 = importerS3;
    }

    public boolean deleteImportFile(FileType fileType, String identifier, String extension)
            throws S3FileManagerException {
        return deleteFile(fileType, identifier, extension, getBucketImp());
    }

    public String getImportFileUrl(FileType fileType, String identifier, String extension)
            throws MalformedURLException {
        return getUrl(getBucketImp(), fileType, identifier, extension);
    }

    public String uploadImportFile(File file, FileType fileType, String identifier, String extension)
            throws S3FileManagerException {
        return uploadFile(file, fileType, identifier, extension, getBucketImg());
    }

    @Override
    public ImporterS3 getS3() {
        return this.importerS3;
    }

    @Override
    public String getBucketSharedDoc() {
        return null;
    }
}

