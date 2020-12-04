package de.immomio.importer.worker.s3;

import de.immomio.common.amazon.s3.AbstractS3ImporterFileManager;
import de.immomio.common.amazon.s3.S3FileManagerException;
import de.immomio.data.base.type.common.FileType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.MalformedURLException;

@Service
public class ImporterWorkerS3FileManager extends AbstractS3ImporterFileManager<ImporterWorkerS3> {

    private final ImporterWorkerS3 importerWorkerS3;

    @Autowired
    public ImporterWorkerS3FileManager(ImporterWorkerS3 importerWorkerS3) {
        this.importerWorkerS3 = importerWorkerS3;
    }

    @Override
    public ImporterWorkerS3 getS3() {
        return this.importerWorkerS3;
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
        return uploadFile(file, fileType, identifier, extension, getBucketImp());
    }
}
