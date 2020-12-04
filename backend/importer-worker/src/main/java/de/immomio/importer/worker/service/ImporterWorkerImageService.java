package de.immomio.importer.worker.service;

import de.immomio.cloud.service.s3.AbstractImageService;
import de.immomio.importer.worker.s3.ImporterWorkerS3FileManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImporterWorkerImageService extends AbstractImageService {

    @Autowired
    private ImporterWorkerS3FileManager s3FileManager;

    @Override
    public ImporterWorkerS3FileManager getS3FileManager() {
        return this.s3FileManager;
    }
}
