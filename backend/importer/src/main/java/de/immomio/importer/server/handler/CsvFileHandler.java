package de.immomio.importer.server.handler;

import de.immomio.common.upload.FileStoreService;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.importlog.ImportLog;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.importer.ImportObject;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.importer.converter.csv.CSVToImportObjectConverter;
import de.immomio.constants.exceptions.MissingHeaderKeysException;
import de.immomio.importer.server.handler.util.AsyncUploadExecutor;
import de.immomio.importer.service.IdExtractor;
import de.immomio.importer.service.ImportLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.google.common.io.Files.createTempDir;

/**
 * @author Maik Kingma
 */

@Slf4j
@Service
public class CsvFileHandler {

    private final CSVToImportObjectConverter csvToImportObjectConverter;

    private final ImportLogService importLogService;

    private final IdExtractor idExtractor;

    private final RestTemplate restTemplate;

    private final FileHandler fileHandler;

    private final AsyncUploadExecutor asyncUploadExecutor;

    @Value("${api.url}")
    private String apiUrl;

    @Value("${api.import-path}")
    private String importPath;

    @Autowired
    public CsvFileHandler(
            CSVToImportObjectConverter csvToImportObjectConverter,
            ImportLogService importLogService,
            IdExtractor idExtractor,
            RestTemplate restTemplate,
            FileHandler fileHandler,
            AsyncUploadExecutor asyncUploadExecutor
    ) {
        this.csvToImportObjectConverter = csvToImportObjectConverter;
        this.importLogService = importLogService;
        this.idExtractor = idExtractor;
        this.restTemplate = restTemplate;
        this.fileHandler = fileHandler;
        this.asyncUploadExecutor = asyncUploadExecutor;
    }

    void convertAndStoreCSVFile(File file, EntityModel<LandlordCustomer> customerResource, Set<File> attachments) {
        List<Property> properties = new ArrayList<>();
        EntityModel<ImportLog> importLogResource = importLogService.initImportLog(customerResource);

        try {
            properties = csvToImportObjectConverter.convert(file, attachments);
        } catch (IOException e) {
            log.error("Error retrieving data from File.", e);
            importLogService.importErrorLog(importLogResource, e);
            return;
        } catch (MissingHeaderKeysException ex) {
            log.error(ex.getMessage(), ex);
            importLogService.importErrorLog(importLogResource, ex);
        }

        Long customerId = idExtractor.extractIDFromResource(customerResource);
        Long importLogId = idExtractor.extractIDFromResource(importLogResource);
        File destinationFolder = createTempDir();

        properties.stream().map(property -> {
            try {
                return createImportObject(property, destinationFolder, customerId, importLogId, importLogResource);
            } catch (Exception ex) {
                log.error("Error converting flat for import.", ex);
                importLogService.importErrorLog(importLogResource, ex);

                return null;
            }
        }).filter(Objects::nonNull).forEach(importObject -> {
            HttpEntity<ImportObject> httpEntity = new HttpEntity<>(importObject);

            log.info("Uploading importObject with Flat: " + importObject.getProperty().getData().getName());

            restTemplate.exchange(apiUrl + importPath, HttpMethod.POST, httpEntity, ImportObject.class);
        });
    }

    private ImportObject createImportObject(
            Property property,
            File destinationFolder,
            Long customerId,
            Long importLogId,
            EntityModel<ImportLog> importLogResource
    ) {
        List<S3File> s3Files = fileHandler.updateAndCopyAttachment(
                property.getData().getAttachments(),
                destinationFolder);
        property.getData().setAttachments(s3Files);

        s3Files = fileHandler.updateAndCopyAttachment(property.getData().getDocuments(), destinationFolder);
        property.getData().setDocuments(s3Files);

        File tempDir = createTempDir();
        File zipFile = new File(tempDir, UUID.randomUUID().toString() + ".zip");

        fileHandler.getFileZipper().zipFiles(destinationFolder, zipFile, false);

        String identifier = FileStoreService.generateIdentifier();
        String zipUrl = asyncUploadExecutor.execute(zipFile, identifier);

        ImportObject importObject = fileHandler.createImportObject(property, customerId, null, importLogId, null);
        importLogService.incrementLogSize(importLogResource);

        S3File zippedAttachments = fileHandler.createZippedAttachments(identifier, zipUrl);
        importObject.setAttachments(zippedAttachments);

        return importObject;
    }
}
