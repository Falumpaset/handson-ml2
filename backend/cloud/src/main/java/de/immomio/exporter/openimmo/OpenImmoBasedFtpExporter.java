package de.immomio.exporter.openimmo;

import de.immomio.common.xml.NoNamesWriter;
import de.immomio.common.zip.FileZipper;
import de.immomio.constants.exceptions.FtpUploadException;
import de.immomio.data.base.type.portal.Portal;
import de.immomio.data.landlord.entity.credential.Credential;
import de.immomio.data.landlord.entity.property.Property;
import de.immomio.exporter.AbstractExporter;
import de.immomio.exporter.amazon.s3.BrokerS3FileManager;
import de.immomio.exporter.config.FtpProperties;
import de.immomio.exporter.config.FtpType;
import de.immomio.exporter.exception.ExporterException;
import de.immomio.exporter.openimmo.converter.OpenImmoConverter;
import de.immomio.exporter.openimmo.upload.AbstractFtpUploader;
import de.immomio.exporter.openimmo.upload.FtpUploader;
import de.immomio.exporter.openimmo.upload.FtpsUploader;
import de.immomio.exporter.openimmo.upload.SftpUploader;
import de.immomio.openimmo.Openimmo;
import de.immomio.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Johannes Hiemer.
 * @author Bastian Bliemeister
 */

@Slf4j
public abstract class OpenImmoBasedFtpExporter extends AbstractExporter {

    private static final String IMMOMIO_STAGE = "immomio_stage";
    private static final String OPEN_IMMO_EXPORT_XML = "openImmoExport.xml";
    private final OpenImmoConverter converter;

    @Value("${encryption.credentials.key}")
    private String encryptionKey;

    @Autowired
    private FileZipper fileZipper;

    @Autowired
    private FtpUploader ftpUploader;

    @Autowired
    private FtpsUploader ftpsUploader;

    @Autowired
    private SftpUploader sftpUploader;

    @Autowired
    private LocationService locationService;

    @Autowired
    private BrokerS3FileManager s3DownloadManager;

    protected abstract FtpProperties getFtpProperties(Credential credential, String encryptionKey);

    protected OpenImmoBasedFtpExporter(Portal portal, OpenImmoConverter converter) {
        super(portal);

        this.converter = converter;
    }

    private static String generateExportFilename(Property property) {
        return property.getId().toString() + "_" + System.currentTimeMillis() + ".zip";
    }

    @Override
    public BrokerS3FileManager getS3FileManager() {
        return s3DownloadManager;
    }

    private void uploadAndCleanup(Property property, File folder, FtpProperties ftpProperties)
            throws IOException, FtpUploadException {
        File zipFileFolder = this.getTempDir();
        String filename = generateExportFilename(property);
        String path = zipFileFolder.getAbsolutePath() + File.separator + filename;

        File zippedFile = new File(path);

        fileZipper.zipFiles(folder.getAbsolutePath(), zippedFile, false);

        log.info(getPortal().name() + " -> Starting upload for flat: " + property.getData().getName());

        try {
            uploadAndCleanupByType(ftpProperties, path, filename);
        } finally {
            deleteDirectory(zipFileFolder, property);
            deleteDirectory(folder, property);
        }
    }

    @Override
    public boolean activate(Property property, Credential credential) throws ExporterException {
        try {
            log.info(getPortal().name() + " -> Starting export for: " + property);

            process(property, credential);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ExporterException(getPortal().name() + " -> threw Exception during export", e);
        }

        return true;
    }

    @Override
    public boolean update(Property property, Credential credential) throws ExporterException {
        try {
            log.info(getPortal().name() + " -> Starting export for: " + property);

            process(property, credential);
        } catch (Exception e) {
            throw new ExporterException(getPortal().name() + " -> threw Exception during export", e);
        }

        return true;
    }

    @Override
    public boolean deactivate(Property property, Credential credential) throws ExporterException {
        try {
            log.info(getPortal().name() + " -> Starting export for: " + property);

            unprocess(property, credential);
        } catch (Exception e) {
            throw new ExporterException(getPortal().name() + " -> threw Exception during export", e);
        }

        return true;
    }

    @Override
    public boolean delete(Property property, Credential credential)
            throws ExporterException {
        try {
            log.info(getPortal().name() + " -> Starting export for: " + property);

            unprocess(property, credential);
        } catch (Exception e) {
            throw new ExporterException(getPortal().name() + " -> threw Exception during export", e);
        }

        return true;
    }

    // TODO: 2020-07-29 remove useExternalId logic. This was implemented due to wrong ids in the portals
    private void unprocess(Property property, Credential credential) throws JAXBException, IOException,
            FtpUploadException, XMLStreamException, ExporterException {
        unprocessHelper(property, credential, true);
        unprocessHelper(property, credential, false);
    }

    protected void unprocessHelper(Property property, Credential credential, boolean useExternalId) throws JAXBException, IOException,
            IllegalArgumentException, SecurityException, FtpUploadException, XMLStreamException, ExporterException {

        File folder = getTempDir();
        FtpProperties ftpProperties = getFtpProperties(credential, encryptionKey);

        Openimmo openImmo = converter.convertForUnprocess(property, ftpProperties.getUsername(),
                credential.getPortal(), getApplicationUrl(property), getObjectDescriptionWithDisclaimer(property), useExternalId);

        File file = new File(folder.getAbsoluteFile() + File.separator + "openImmoExport.xml");

        Marshaller jaxbMarshaller = getMarshaller();
        jaxbMarshaller.marshal(openImmo, NoNamesWriter.filter(new FileWriter(file)));

        this.uploadAndCleanup(property, folder, ftpProperties);
    }

    protected void process(Property property, Credential credential) throws JAXBException, IOException,
            IllegalArgumentException, SecurityException, FtpUploadException, XMLStreamException, ExporterException {

        FtpProperties ftpProperties = getFtpProperties(credential, encryptionKey);
        String objectDescriptionWithDisclaimer = getObjectDescriptionWithDisclaimer(property);
        String miscellaneousText = getObjectMiscellaneousTextWithApplicationLink(property);

        log.info("######### objectDescriptionWithDisclaimer: " + objectDescriptionWithDisclaimer);
        log.info("######### miscellaneousText: " + miscellaneousText);

        Openimmo openImmo = converter.convertForProcess(
                property,
                ftpProperties.getUsername(),
                credential.getPortal(),
                getApplicationUrl(property),
                objectDescriptionWithDisclaimer,
                miscellaneousText,
                true);

        File folder = this.getTempDir();
        File file = new File(folder.getAbsoluteFile() + File.separator + OPEN_IMMO_EXPORT_XML);

        Marshaller jaxbMarshaller = getMarshaller();
        jaxbMarshaller.marshal(openImmo, NoNamesWriter.filter(new FileWriter(file)));

        this.downloadAttachments(property, folder);

        this.uploadAndCleanup(property, folder, ftpProperties);
    }

    public boolean checkConnection(Credential credential) {
        if (credential == null || credential.getPortal() != getPortal()) {
            return false;
        }

        FtpProperties ftpProperties = getFtpProperties(credential, encryptionKey);
        return checkConnectionByType(ftpProperties);
    }

    private boolean checkConnectionByType(FtpProperties ftpProperties) {
        try {
            if (ftpProperties.getType() != null) {
                switch (ftpProperties.getType()) {
                    case SFTP:
                        return sftpUploader.checkConnection(ftpProperties.getHost(), ftpProperties.getPort(),
                                ftpProperties.getUsername(), ftpProperties.getPassword());
                    case FTPS:
                        return ftpsUploader.checkConnection(ftpProperties.getHost(), ftpProperties.getPort(),
                                ftpProperties.getUsername(), ftpProperties.getPassword());
                    default:
                        return ftpUploader.checkConnection(ftpProperties.getHost(), ftpProperties.getPort(),
                                ftpProperties.getUsername(), ftpProperties.getPassword());
                }
            } else {
                return ftpUploader.checkConnection(ftpProperties.getHost(), ftpProperties.getPort(),
                        ftpProperties.getUsername(), ftpProperties.getPassword());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    private void uploadAndCleanupByType(FtpProperties ftpProperties, String path, String filename)
            throws IOException, FtpUploadException {
        if (ftpProperties.getType() != null) {
            switch (ftpProperties.getType()) {
                case SFTP:
                    uploadAndCleanup(sftpUploader, ftpProperties, path, filename);
                    break;
                case FTPS:
                    uploadAndCleanup(ftpsUploader, ftpProperties, path, filename);
                    break;
                default:
                    uploadAndCleanup(ftpUploader, ftpProperties, path, filename);
            }
        } else {
            uploadAndCleanup(ftpUploader, ftpProperties, path, filename);
        }
    }

    private void uploadAndCleanup(AbstractFtpUploader uploader, FtpProperties ftpProperties, String path,
                                  String filename) throws IOException, FtpUploadException {
        String host = ftpProperties.getHost();
        int port = ftpProperties.getPort();
        String type = ftpProperties.getType() != null ? ftpProperties.getType().name() : FtpType.FTP.name();
        String errorMsg = "Unsuccessfully uploaded file via " + type + " -> " + host + ":" + port;
        if (!uploader.configure(host, port, ftpProperties.getUsername(), ftpProperties.getPassword())) {
            throw new FtpUploadException(errorMsg);
        }

        if (!uploader.upload(path, filename)) {
            throw new FtpUploadException(errorMsg);
        }
    }

    private Marshaller getMarshaller() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Openimmo.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "");
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        return marshaller;
    }

    private void deleteDirectory(File folder, Property property) {
        try {
            FileUtils.deleteDirectory(folder);
        } catch (Exception e) {
            String path = folder.getAbsolutePath();
            log.error("Error deleting folder [" + path + "] while exporting flat [" + property + "]", e);
        }
    }

}
