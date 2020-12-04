package de.immomio.docusign.service;

import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.Envelope;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeDocument;
import com.docusign.esign.model.EnvelopeDocumentsResult;
import com.docusign.esign.model.EnvelopeSummary;
import com.sun.jersey.core.util.Base64;
import de.immomio.common.amazon.s3.S3FileManagerException;
import de.immomio.common.file.FileUtilities;
import de.immomio.constants.exceptions.DocuSignApiException;
import de.immomio.data.base.type.common.FileType;
import de.immomio.data.shared.bean.common.S3File;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.docusign.service.amazon.s3.DocuSignS3FileManager;
import de.immomio.docusign.service.beans.DigitalContractEmbeddedSendingBean;
import de.immomio.utils.FileStorageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class DocuSignService {

    private static final String ENVELOPE_STATUS_CREATED = "created";
    private static final String ENVELOPE_STATUS_VOIDED = "voided";
    private static final String EXTENSION_PDF = "pdf";
    private static final String EXTENSION_ZIP = "zip";
    private static final String DOT = ".";
    private static final String EXTENSION_DOT_PDF = DOT + EXTENSION_PDF;
    private static final String SEPARATOR = "_";
    private static final String DOCUSIGN_EMAIL_SUBJECT = "The email subject is mandatory in DocuSign.";
    private static final String REQUEST_FOR_DELETION_OF_CONTRACT_FROM_LL = "REQUEST_FOR_DELETION_OF_CONTRACT_FROM_LL";

    private final DocuSignS3FileManager docuSignS3FileManager;
    private final DocuSignSignerService docuSignSignerService;
    private final DocuSignApiService docuSignApiService;

    @Autowired
    public DocuSignService(
            DocuSignS3FileManager docuSignS3FileManager,
            DocuSignSignerService docuSignSignerService,
            DocuSignApiService docuSignApiService
    ) {
        this.docuSignS3FileManager = docuSignS3FileManager;
        this.docuSignSignerService = docuSignSignerService;
        this.docuSignApiService = docuSignApiService;
    }

    public DigitalContractEmbeddedSendingBean createEnvelope(
            DigitalContract digitalContract,
            String redirectUrl,
            UUID apiUserId,
            List<DigitalContractSigner> digitalContractSigners
    ) throws DocuSignApiException {
        EnvelopeDefinition envelopeDefinition = createEnvelopeDefinition(digitalContract, digitalContractSigners);
        try {
            EnvelopeSummary envelopeSummary = docuSignApiService.apiCreateEnvelope(apiUserId, envelopeDefinition);

            String envelopeId = envelopeSummary.getEnvelopeId();
            digitalContract.setDocuSignEnvelopeId(UUID.fromString(envelopeId));

            return docuSignApiService.getEmbeddedSendingUrl(apiUserId, redirectUrl, envelopeId);
        } catch (ApiException e) {
            log.error(e.getMessage(), e);

            throw new DocuSignApiException(e.getMessage(), e.getResponseBody());
        }
    }

    public DigitalContractEmbeddedSendingBean updateEnvelope(
            DigitalContract digitalContract,
            String redirectUrl,
            UUID apiUserId,
            List<DigitalContractSigner> digitalContractSigners
    ) throws DocuSignApiException {
        try {
            voidEnvelope(digitalContract.getDocuSignEnvelopeId(), apiUserId);
        } catch (Exception e) {
            log.error("envelope could not be voided " + digitalContract.getDocuSignEnvelopeId());
        }
        return createEnvelope(digitalContract, redirectUrl, apiUserId, digitalContractSigners);
    }

    public void voidEnvelope(UUID envelopeId, UUID apiUserId) throws DocuSignApiException {
        Envelope envelope = new Envelope();
        envelope.setStatus(ENVELOPE_STATUS_VOIDED);
        envelope.setVoidedReason(REQUEST_FOR_DELETION_OF_CONTRACT_FROM_LL);

        try {
            docuSignApiService.apiVoidEnvelope(apiUserId, envelopeId.toString(), envelope);
        } catch (ApiException e) {
            log.error(e.getMessage(), e);

            throw new DocuSignApiException(e.getMessage(), e.getResponseBody());
        }
    }

    public DigitalContractEmbeddedSendingBean getSignatureMappingUrl(
            UUID envelopeId,
            String redirectUrl,
            UUID apiUserId
    ) throws DocuSignApiException {

        return docuSignApiService.getEmbeddedSendingUrl(apiUserId, redirectUrl, envelopeId.toString());
    }

    public void loadAndSaveSignedDocuments(DigitalContract digitalContract, String filenameWithoutEnding) {
        getSignedDocumentCombinedFile(digitalContract, filenameWithoutEnding);

        getSignedDocumentSingleFiles(digitalContract, filenameWithoutEnding);
    }

    private void getSignedDocumentCombinedFile(DigitalContract digitalContract, String fileName) {
        String envelopeIdStr = digitalContract.getDocuSignEnvelopeId().toString();
        String documentId = DocuSignApiService.DOCUSIGN_DOCUMENT_ID_COMBINED;
        String s3Identifier = digitalContract.getInternalContractId() + SEPARATOR + documentId;
        try {
            byte[] documentBytes = docuSignApiService.apiGetDocument(envelopeIdStr, documentId);

            digitalContract.setSignedDocumentCombinedFile(uploadDocumentToS3(fileName, s3Identifier, documentBytes, EXTENSION_PDF));
        } catch (ApiException e) {
            log.error("Loading of signed document failed envelopeId: {}, documentId: {} ", envelopeIdStr, documentId);
            log.error(e.getMessage());
        }
    }

    private void getSignedDocumentSingleFiles(DigitalContract digitalContract, final String filenameWithoutEnding) throws DocuSignApiException {
        String envelopeIdStr = digitalContract.getDocuSignEnvelopeId().toString();
        String internalContractIdStr = digitalContract.getInternalContractId().toString();
        List<S3File> singleFiles = new ArrayList<>();
        Map<String, byte[]> tenantDocumentsForArchive = new HashMap<>();
        Map<String, byte[]> landlordDocumentsForArchive = new HashMap<>();

        try {
            EnvelopeDocumentsResult documentsResult = docuSignApiService.apiListDocuments(envelopeIdStr);
            if (documentsResult != null) {
                List<EnvelopeDocument> envelopeDocuments = documentsResult.getEnvelopeDocuments();
                if (envelopeDocuments != null && !envelopeDocuments.isEmpty()) {
                    envelopeDocuments.forEach(envelopeDocument -> {
                        String documentId = envelopeDocument.getDocumentId();
                        String documentName = envelopeDocument.getName();
                        if (documentName != null) {
                            documentName = documentName.replace(EXTENSION_DOT_PDF, "");
                        }
                        String singleFilename = documentName + SEPARATOR + filenameWithoutEnding + SEPARATOR + documentId;
                        String s3Identifier = internalContractIdStr + SEPARATOR + documentId;
                        try {
                            byte[] documentBytes = docuSignApiService.apiGetDocument(envelopeIdStr, documentId);
                            if (!DocuSignApiService.DOCUSIGN_DOCUMENT_ID_CERTIFICATE.equals(documentId)) {
                                tenantDocumentsForArchive.put(singleFilename, documentBytes);
                            }
                            landlordDocumentsForArchive.put(singleFilename, documentBytes);

                            singleFiles.add(uploadDocumentToS3(singleFilename, s3Identifier, documentBytes, EXTENSION_PDF));
                        } catch (ApiException e) {
                            log.error("Loading of signed document failed envelopeId: {}, documentId: {} ", envelopeIdStr, documentId);
                            log.error(e.getMessage());
                        }
                    });
                }
            }

            S3File tenantArchive = createArchive(tenantDocumentsForArchive, filenameWithoutEnding, internalContractIdStr,
                    DocuSignApiService.DOCUSIGN_DOCUMENT_ID_ARCHIVE);
            digitalContract.setSignedDocumentArchiveFile(tenantArchive);

            S3File landlordArchive = createArchive(landlordDocumentsForArchive, filenameWithoutEnding, internalContractIdStr,
                    DocuSignApiService.DOCUSIGN_DOCUMENT_ID_ARCHIVE_CERTIFICATE);
            digitalContract.setSignedDocumentArchiveCertificateFile(landlordArchive);

            digitalContract.setSignedDocumentSingleFiles(singleFiles);
        } catch (ApiException e) {
            log.error("Loading of signed document failed envelopeId: {}", envelopeIdStr);

            throw new DocuSignApiException(e.getMessage(), e.getResponseBody());
        }
    }

    private EnvelopeDefinition createEnvelopeDefinition(
            DigitalContract digitalContract,
            List<DigitalContractSigner> digitalContractSigners
    ) {
        EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();

        envelopeDefinition.setEmailSubject(DOCUSIGN_EMAIL_SUBJECT);
        addDocuments(envelopeDefinition, digitalContract);
        envelopeDefinition.setRecipients(docuSignSignerService.createRecipients(digitalContract, digitalContractSigners));

        // Request that the envelope be sent by setting |status| to "sent".
        // To request that the envelope be created as a draft, set to "created"
        envelopeDefinition.setStatus(ENVELOPE_STATUS_CREATED);
        return envelopeDefinition;
    }

    private void addDocuments(EnvelopeDefinition envelopeDefinition, DigitalContract digitalContract) {
        List<S3File> s3FileList = digitalContract.getDocumentFiles();
        final AtomicInteger documentId = new AtomicInteger(1);
        s3FileList.forEach(s3File -> {
            File file = null;
            Document doc = new Document();
            try {
                file = FileStorageUtils.downloadFile(
                        s3File.getUrl(),
                        docuSignS3FileManager,
                        s3File.isEncrypted(),
                        null);
                doc.setDocumentBase64(new String(Base64.encode(Files.readAllBytes(file.toPath()))));
                doc.setName(s3File.getName());
                doc.setFileExtension(s3File.getExtension());
                doc.setDocumentId(String.valueOf(documentId.getAndIncrement()));

                envelopeDefinition.addDocumentsItem(doc);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            } finally {
                if (file != null) {
                    FileUtilities.forceDelete(file);
                }
            }
        });
    }

    private S3File createArchive(Map<String, byte[]> documentsForArchive, String filenameWithoutEnding, String internalContractId, String nameExtension) {
        String s3Identifier = internalContractId + SEPARATOR + nameExtension;
        String archiveFilename = filenameWithoutEnding + SEPARATOR + nameExtension;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ZipOutputStream zipOut = new ZipOutputStream(bos)) {
            documentsForArchive.forEach((filename, documentBytes) -> {
                try {
                    ZipEntry zipEntry = new ZipEntry(filename + DOT + EXTENSION_PDF);
                    zipOut.putNextEntry(zipEntry);
                    zipOut.write(documentBytes);
                } catch (IOException ex) {
                    log.error("Creating of archive failed.");
                }
            });
            zipOut.close();
            byte[] zipContent = bos.toByteArray();

            return uploadDocumentToS3(archiveFilename, s3Identifier, zipContent, EXTENSION_ZIP);
        } catch (IOException e) {
            log.error("Creating of archive failed.");
        }

        return null;
    }

    private S3File uploadDocumentToS3(String filenameWithoutEnding, String s3Identifier, byte[] documentBytes, String fileExtension) {
        try {
            String dotExtension = DOT + fileExtension;
            String filename = filenameWithoutEnding + dotExtension;
            File folder = com.google.common.io.Files.createTempDir();
            File file = new File(folder, s3Identifier + dotExtension);
            Files.write(file.toPath(), documentBytes);

            String s3FileUrl = docuSignS3FileManager.uploadContract(
                    file,
                    FileType.SHARED_DOCUMENT,
                    s3Identifier,
                    fileExtension
            );

            S3File s3File = new S3File();
            s3File.setIdentifier(s3Identifier);
            s3File.setType(FileType.SHARED_DOCUMENT);
            s3File.setUrl(s3FileUrl);
            s3File.setExtension(fileExtension);
            s3File.setTitle(filename);
            s3File.setName(filename);

            return s3File;
        } catch (IOException | S3FileManagerException e) {
            log.error(e.getMessage(), e);

            return null;
        }
    }

}
