package de.immomio.importer.server.handler;

import de.immomio.common.file.FileUtilities;
import de.immomio.common.upload.FileStoreService;
import de.immomio.constants.exceptions.OpenImmoToImmomioConverterException;
import de.immomio.data.landlord.bean.property.data.PropertyData;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.data.landlord.entity.importlog.ImportLog;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.data.landlord.entity.property.importer.ImportMailData;
import de.immomio.data.landlord.entity.property.importer.ImportObject;
import de.immomio.data.landlord.entity.property.openimmo.OpenImmoAktionTyp;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.exporter.openimmo.feedback.ImmoBlueConstants;
import de.immomio.importer.converter.openimmo.OpenImmoToImmomioConverter;
import de.immomio.importer.server.handler.util.AsyncUploadExecutor;
import de.immomio.importer.server.handler.util.ImporterUploadException;
import de.immomio.importer.service.IdExtractor;
import de.immomio.importer.service.ImportLogService;
import de.immomio.openimmo.Anbieter;
import de.immomio.openimmo.Immobilie;
import de.immomio.openimmo.Openimmo;
import de.immomio.openimmo.UserDefinedSimplefield;
import de.immomio.openimmo.VerwaltungObjekt;
import de.immomio.openimmo.VerwaltungTechn;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.google.common.io.Files.createTempDir;

/**
 * @author Maik Kingma
 */

@Slf4j
@Service
public class PropertyDispatcher {

    private static final String OBJEKT_NR = "ObjektNr";

    private static final String DOT_ZIP = ".zip";
    public static final String PROPERTY_MANAGER_EMAIL = "property_manager_email";

    private final ImportLogService importLogService;

    private final RestTemplate restTemplate;

    private final IdExtractor idExtractor;

    private final FileHandler fileHandler;

    private final AsyncUploadExecutor asyncUploadExecutor;

    private final OpenImmoToImmomioConverter openImmoToImmomioConverter;

    @Value("${api.url}")
    private String apiUrl;

    @Value("${api.import-path}")
    private String importPath;

    @Autowired
    public PropertyDispatcher(ImportLogService importLogService, RestTemplate restTemplate, IdExtractor idExtractor,
            FileHandler fileHandler, AsyncUploadExecutor asyncUploadExecutor,
            OpenImmoToImmomioConverter openImmoToImmomioConverter) {
        this.importLogService = importLogService;
        this.restTemplate = restTemplate;
        this.idExtractor = idExtractor;
        this.fileHandler = fileHandler;
        this.asyncUploadExecutor = asyncUploadExecutor;
        this.openImmoToImmomioConverter = openImmoToImmomioConverter;
    }

    void convertAndStoreProperty(Openimmo openImmo, EntityModel<LandlordCustomer> customer, File sourceFolder) {
        if (openImmo != null && openImmo.getAnbieter() != null) {
            List<ImportObject> importObjects = new ArrayList<>();
            EntityModel<ImportLog> importLog = importLogService.initImportLog(customer);

            openImmo.getAnbieter()
                    .stream()
                    .map(Anbieter::getImmobilie)
                    .flatMap(Collection::stream)
                    .forEach(data -> handleImmobilienImport(
                            data,
                            openImmo,
                            sourceFolder,
                            customer,
                            importLog,
                            importObjects));

            importObjects.forEach(importObject -> {
                HttpEntity<ImportObject> httpEntity = new HttpEntity<>(importObject);
                restTemplate.exchange(apiUrl + importPath, HttpMethod.POST, httpEntity, ImportObject.class);
            });
        }
    }

    private void handleImmobilienImport(
            Immobilie immobilie,
            Openimmo openImmo,
            File sourceFolder,
            EntityModel<LandlordCustomer> customer,
            EntityModel<ImportLog> importLog,
            List<ImportObject> importObjects
    ) {
        try {
            if (!immobilie.getObjektkategorie().getVermarktungsart().isMIETEPACHT()) {
                log.error("Object is not a rental-flat ... skipping");
                return;
            }

            ImportMailData email = handleEmail(openImmo, immobilie);
            ImportObject importObject = convertProperty(immobilie, sourceFolder, customer, email, importLog);
            Assert.notNull(importObject, "importObject is null");

            importLogService.incrementLogSize(importLog);

            importObjects.add(importObject);
        } catch (Exception ex) {
            log.error("Error converting flat for import.", ex);

            importLogService.importErrorLog(importLog, ex);
        }
    }

    private ImportMailData handleEmail(Openimmo openImmo, Immobilie immobilie) {
        String email;

        if (checkForContactInfoDirect(immobilie)) {
            email = immobilie.getKontaktperson().getEmailDirekt();
        } else if (checkForContactInfoZentrale(immobilie)) {
            email = immobilie.getKontaktperson().getEmailZentrale();
        } else {
            email = openImmo.getUebertragung().getTechnEmail();
        }

        if (StringUtils.isEmpty(email)) {
            log.warn("no email has been found. Using admin email");
        }
        ImportMailData.ImportMailDataBuilder importMailDataBuilder = ImportMailData.builder().agentMail(email);

        immobilie.getUserDefinedSimplefield().stream()
                .filter(userDefinedSimplefield -> PROPERTY_MANAGER_EMAIL.equalsIgnoreCase(userDefinedSimplefield.getFeldname()))
                .map(UserDefinedSimplefield::getContent)
                .findFirst()
                .ifPresent(importMailDataBuilder::propertyManagerMail);

        return importMailDataBuilder.build();
    }

    private boolean checkForContactInfoDirect(Immobilie immobilie) {
        return immobilie.getKontaktperson() != null &&
                StringUtils.hasText(immobilie.getKontaktperson().getEmailDirekt());
    }

    private boolean checkForContactInfoZentrale(Immobilie immobilie) {
        return immobilie.getKontaktperson() != null &&
                StringUtils.hasText(immobilie.getKontaktperson().getEmailZentrale());
    }

    private ImportObject convertProperty(
            Immobilie immobilie,
            File sourceFolder,
            EntityModel<LandlordCustomer> customerResource,
            ImportMailData mailData,
            EntityModel<ImportLog> importLogResource
    ) throws ImporterUploadException, OpenImmoToImmomioConverterException {
        VerwaltungTechn verwaltungTechn = immobilie.getVerwaltungTechn();
        log.info("converting Flat -> " + verwaltungTechn.getObjektnrExtern());
        Property property = openImmoToImmomioConverter.convert(immobilie);

        PropertyData propertyData = property.getData();

        // Immoblue delivers internal immoblueId as the external ID of property in xml thus we need to exchange
        // externalID and reference ID for that customer as it is the actual externalId we need in the ref Id field
        if (idExtractor.extractIDFromResource(customerResource) == ImmoBlueConstants.WOHNBAU_ID) {
            log.info("EXECUTING CUSTOM WOHNBAU LOGIC FOR EXTENRAL ID");

            String externalIdImmoBlue = property.getExternalId();
            property.setExternalId(propertyData.getReferenceId());
            propertyData.setReferenceId(externalIdImmoBlue);

            log.info("new external ID: " + property.getExternalId());
            log.info("new reference ID: " + propertyData.getReferenceId());

        }

        File destinationFolder = createTempDir();

        List<S3File> tmp = propertyData.getAttachments();
        tmp = getFilesAndCopyToDestination(tmp, destinationFolder, sourceFolder);
        propertyData.setAttachments(tmp);

        tmp = propertyData.getDocuments();
        tmp = getFilesAndCopyToDestination(tmp, destinationFolder, sourceFolder);

        propertyData.setDocuments(tmp);

        File zipFile = new File(sourceFolder, UUID.randomUUID().toString() + DOT_ZIP);
        fileHandler.getFileZipper().zipFiles(destinationFolder, zipFile, false);

        String identifier = FileStoreService.generateIdentifier();
        String zipUrl = asyncUploadExecutor.execute(zipFile, identifier);

        if (zipUrl != null) {
            Long customerId = idExtractor.extractIDFromResource(customerResource);
            Long importLogId = idExtractor.extractIDFromResource(importLogResource);
            VerwaltungObjekt verwaltungObjekt = immobilie.getVerwaltungObjekt();
            Boolean wbsSozialwohnung = null;
            if (verwaltungObjekt != null) {
                wbsSozialwohnung = verwaltungObjekt.isWbsSozialwohnung();
            }

            ImportObject importObject = fileHandler.createImportObject(
                    property,
                    customerId,
                    mailData,
                    importLogId,
                    wbsSozialwohnung);

            S3File file = fileHandler.createZippedAttachments(identifier, zipUrl);
            importObject.setAttachments(file);
            FileUtilities.forceDelete(destinationFolder);
            OpenImmoAktionTyp aktionTyp = EnumUtils.getEnumIgnoreCase(OpenImmoAktionTyp.class, verwaltungTechn.getAktion().getAktionart());
            if (aktionTyp == OpenImmoAktionTyp.DELETE) {
               importObject.setImportAction(OpenImmoAktionTyp.DELETE);
            } else {
                importObject.setImportAction(OpenImmoAktionTyp.CHANGE);
            }

            return importObject;
        } else {
            throw new ImporterUploadException("Error during the uploading of the zip to amazon s3 bucket");
        }
    }

    private List<S3File> getFilesAndCopyToDestination(List<S3File> tmp, File destinationFolder, File sourceFolder) {
        return tmp.stream().map(document -> {
            try {
                File file = fileHandler.getFileAndCopyToDestination(document.getUrl().toLowerCase(), destinationFolder,
                        sourceFolder);

                document.setUrl(file.getName());
                return document;
            } catch (Exception e) {
                log.error("Skip document -> " + document.getUrl().toLowerCase(), e);
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
