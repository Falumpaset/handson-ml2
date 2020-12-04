package de.immomio.importer.server.handler;

import de.immomio.common.file.FileUtilities;
import de.immomio.constants.exceptions.ImmomioTechnicalException;
import de.immomio.data.landlord.entity.customer.LandlordCustomer;
import de.immomio.importer.server.ftplet.CheckedFileResult;
import de.immomio.importer.server.ftplet.UserType;
import de.immomio.openimmo.Openimmo;
import de.immomio.openimmo.constants.OpenImmoConstants;
import de.immomio.utils.XmlParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static de.immomio.importer.server.ftplet.CheckedFileResult.INVALID;
import static de.immomio.importer.server.ftplet.CheckedFileResult.VALID;

/**
 * @author Bastian Bliemeister, Maik Kingma, Johannes Strauss
 */
@Slf4j
@Service
public class FileDispatcher {

    private final FileHandler fileHandler;

    private final PropertyDispatcher propertyDispatcher;

    private final CsvFileHandler csvFileHandler;

    @Autowired
    public FileDispatcher(FileHandler fileHandler,
            PropertyDispatcher propertyDispatcher, CsvFileHandler csvFileHandler) {
        this.fileHandler = fileHandler;
        this.propertyDispatcher = propertyDispatcher;
        this.csvFileHandler = csvFileHandler;
    }

    @Async
    public void scanFolder(String homeDir, EntityModel<LandlordCustomer> customer, UserType userType) {
        File folder = new File(homeDir);
        File[] files = folder.listFiles();

        if (files != null && files.length > 0) {
            Arrays.stream(files).forEach(file -> {
                if (checkFileDependingOnWorkerType(file)) {
                    try {
                        if (userType.equals(UserType.RELION)) {
                            dispatchCsvFile(file, customer);
                        } else if (userType.equals(UserType.OPENIMMO)) {
                            dispatchOpenimmoFile(file, customer);
                        }
                    } catch (Exception e) {
                        log.error("Error during import ...", e);
                    }
                }
            });
        }
    }

    private void dispatchOpenimmoFile(File file, EntityModel<LandlordCustomer> customer) {
        try {
            File folder = unzipIntoFolder(file);

            JAXBContext jaxbContext = JAXBContext.newInstance(Openimmo.class);
            this.uploadFileAndAttachmentsOpenimmo(folder, customer, jaxbContext);
            FileUtils.forceDelete(folder);
        } catch (JAXBException e) {
            log.error("Could not load file from import, " + e.getMessage(), e);
        } catch (IOException e) {
            log.error("Could not delete source file from FTP Server, import aborted to avoid duplicates. " +
                    "Cause: " + e.getMessage(), e);
        }
    }

    private void dispatchCsvFile(File file, EntityModel<LandlordCustomer> customer) {
        try {
            File folder = unzipIntoFolder(file);

            Set<File> attachments = fileHandler.getAttachmentsFromFolder(folder);
            uploadFileAndAttachmentsCsv(folder, customer, attachments);
            FileUtils.forceDelete(folder);
        } catch (Exception e) {
            log.error("An unexpected Error occurred: " + e.getMessage(), e);
        }
    }

    private File unzipIntoFolder(File file) throws IOException {
        File folder = new File(FileUtils.getTempDirectoryPath() + "/import/" + UUID.randomUUID());
        if (file.exists()) {
            try {
                fileHandler.getFileZipper().unzipFile(file.getAbsolutePath(), folder);
                FileUtils.forceDelete(file);
            } catch (Exception e) {
                if (folder.exists()) {
                    log.info("cleanup unzipped directory, import aborted because unzip failed");
                    FileUtilities.forceDelete(folder);
                }
                throw e;
            }
        }

        return folder;
    }

    private Boolean checkFileDependingOnWorkerType(File file) {
        return checkFile(file).equals(VALID);
    }

    private CheckedFileResult checkFile(File file) {
        if (file == null) {
            log.warn("File to check is null ...");
            return INVALID;
        } else if (!file.isFile() && !file.isDirectory()) {
            log.warn("File to check isn't a file [" + file.getAbsolutePath() + "]");
            return INVALID;
        }

        if (FilenameUtils.isExtension(file.getName(), "zip")) {
            try (ZipFile zFile = new ZipFile(file.getAbsolutePath())) {
                Enumeration<? extends ZipEntry> entries = zFile.entries();
                while (entries.hasMoreElements()) {
                    entries.nextElement();
                }
            } catch (Exception e) {
                log.error("Failed with: ", e);
                return INVALID;
            }
        }
        log.warn("File checked [" + file.getAbsolutePath() + "]");
        return VALID;
    }

    private void uploadFileAndAttachmentsCsv(File file, EntityModel<LandlordCustomer> customer, Set<File> attachments) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            log.info("Scan folder -> " + file.getPath());

            if (files != null) {
                Arrays.stream(files).forEach(currentFile -> {
                    uploadFileAndAttachmentsCsv(currentFile, customer, attachments);
                });
            }
        } else {
            log.info("Found file -> " + file.getAbsolutePath());

            if (checkForValidFile(file)) {
                try {
                    csvFileHandler.convertAndStoreCSVFile(file, customer, attachments);
                } catch (Exception e) {
                    log.error("Error converting/uploading Flat.", e);
                }
            }
        }
    }

    private boolean checkForValidFile(File file) {
        boolean isCsv = FilenameUtils.isExtension(file.getAbsolutePath(), "csv");
        try (InputStream inputStream = new FileInputStream(file.getAbsolutePath());
             Reader reader = new InputStreamReader(inputStream)
        ) {
            CSVParser csvParser;
            csvParser = new CSVParser(reader, CSVFormat.INFORMIX_UNLOAD.withFirstRecordAsHeader());

            if (csvParser.getRecords().isEmpty()) {
                log.warn("unsupported file detected. Skipping");

                return false;
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return isCsv;
    }

    private void uploadFileAndAttachmentsOpenimmo(File folder, EntityModel<LandlordCustomer> customer,
                                                  JAXBContext jaxbContext) {
        log.info("Scan folder -> " + folder.getPath());

        File[] files = folder.listFiles();
        if (files != null) {
            Arrays.stream(files).forEach(file -> {
                if (file.isDirectory()) {
                    this.uploadFileAndAttachmentsOpenimmo(file, customer, jaxbContext);
                }

                if (!FilenameUtils.isExtension(file.getAbsolutePath(), "xml")) {
                    return;
                }

                log.info("Found file -> " + file.getAbsolutePath());

                Openimmo openImmo;
                try {
                    openImmo = XmlParser.parseXML(file, jaxbContext, OpenImmoConstants.OPENIMMO_NAMESPACE, Openimmo.class);
                } catch (ImmomioTechnicalException e) {
                    log.error("Error converting/uploading Flat. due to " + e.getMessage(), e);
                    return;
                }

                Assert.notNull(openImmo, "XML-Content is null");

                String modus = openImmo.getUebertragung().getModus();
                if (modus != null && modus.equals("DELETE")) {
                    return;
                }

                try {
                    propertyDispatcher.convertAndStoreProperty(openImmo, customer, folder);
                } catch (Exception e) {
                    log.error("Error converting/uploading Property.", e);
                }
            });

        }
    }



}
