package de.immomio.docusign.service;

import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.api.TemplatesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.CorrectViewRequest;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.DocumentVisibilityList;
import com.docusign.esign.model.Envelope;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeDocument;
import com.docusign.esign.model.EnvelopeDocumentsResult;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.EnvelopesInformation;
import com.docusign.esign.model.RecipientViewRequest;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.RecipientsUpdateSummary;
import com.docusign.esign.model.ReturnUrlRequest;
import com.docusign.esign.model.Tabs;
import com.docusign.esign.model.ViewUrl;
import de.immomio.constants.exceptions.DocuSignApiException;
import de.immomio.docusign.service.beans.DigitalContractEmbeddedSendingBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DocuSignApiService {

    public static final String DOCUSIGN_DOCUMENT_ID_COMBINED = "combined";
    public static final String DOCUSIGN_DOCUMENT_ID_ARCHIVE = "archive";
    public static final String DOCUSIGN_DOCUMENT_ID_ARCHIVE_CERTIFICATE = "archive_certificate";
    public static final String DOCUSIGN_DOCUMENT_ID_CERTIFICATE = "certificate";
    private static final String OPTIONS_INCLUDE_RECIPIENTS_DOCUMENTS = "recipients,documents";
    private static final String LOGGING_DOCUSIGN_API_CALL_START = "DocuSign api call start {}";
    private static final String LOGGING_DOCUSIGN_API_CALL_FINISHED = "DocuSign api call finished {}, {}";
    private static final String LOGGING_DOCUSIGN_API_LIST_STATUS_CHANGES = "envelopesApi.listStatusChanges";
    private static final String LOGGING_DOCUSIGN_CREATE_ENVELOPE = "envelopesApi.createEnvelope";
    private static final String LOGGING_DOCUSIGN_UPDATE_ENVELOPE = "envelopesApi.updateEnvelope";
    private static final String LOGGING_DOCUSIGN_LIST_DOCUMENTS = "envelopesApi.listDocuments";
    private static final String LOGGING_DOCUSIGN_GET_DOCUMENT = "envelopesApi.getDocument";
    private static final String LOGGING_DOCUSIGN_CREATE_SENDER_VIEW = "envelopesApi.createSenderView";
    private static final String LOGGING_DOCUSIGN_VOID_ENVELOPE = "void envelope with envelopesApi.update";
    private static final String LOGGING_DOCUSIGN_CREATE_TABS = "envelopesApi.createTabs";
    private static final String LOGGING_DOCUSIGN_CREATE_RECIPIENT_VIEW = "envelopesApi.createRecipientView";
    private static final String LOGGING_DOCUSIGN_UPDATE_DOCUMENTS = "envelopesApi.updateDocuments";
    private static final String LOGGING_DOCUSIGN_LIST_RECIPIENTS_FROM_TEMPLATE = "templatesApi.listRecipients";
    private static final String LOGGING_DOCUSIGN_LIST_RECIPIENTS_FROM_ENVELOPE = "envelopesApi.listRecipients";
    private static final String LOGGING_DOCUSIGN_UPDATE_DOCUMENT_VISIBILITY = "envelopesApi.updateRecipientsDocumentVisibility";
    private static final String LOGGING_DOCUSIGN_LIST_TABS = "envelopesApi.listTabs";
    private static final String LOGGING_DOCUSIGN_UPDATE_TABS = "envelopesApi.updateTabs";
    private static final String LANGUAGE_OF_CERTIFICATE = "de";
    private final DocuSignConfig docuSignConfig;
    private final DocuSignApiClient docuSignApiClient;

    @Autowired
    public DocuSignApiService(DocuSignConfig docuSignConfig, DocuSignApiClient docuSignApiClient) {
        this.docuSignConfig = docuSignConfig;
        this.docuSignApiClient = docuSignApiClient;
    }

    public EnvelopesInformation apiListStatusChanges(String envelopeIdsAsCommaSeparatedString) throws ApiException {
        String accountId = docuSignApiClient.getAccountId();
        ApiClient apiClient = docuSignApiClient.getApiClient(docuSignConfig.getAdminUserId());
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        EnvelopesApi.ListStatusChangesOptions options = envelopesApi.new ListStatusChangesOptions();
        options.setEnvelopeIds(envelopeIdsAsCommaSeparatedString);
        options.setInclude(OPTIONS_INCLUDE_RECIPIENTS_DOCUMENTS);

        reportDocuSignApiCallStart(LOGGING_DOCUSIGN_API_LIST_STATUS_CHANGES);
        EnvelopesInformation envelopesInformation = envelopesApi.listStatusChanges(accountId, options);
        reportDocuSignApiCallFinished(LOGGING_DOCUSIGN_API_LIST_STATUS_CHANGES, apiClient);

        return envelopesInformation;
    }

    public byte[] apiGetDocument(String envelopeIdStr, String documentId) throws ApiException {
        String accountId = docuSignApiClient.getAccountId();
        ApiClient apiClient = docuSignApiClient.getApiClient(docuSignConfig.getAdminUserId());
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        reportDocuSignApiCallStart(LOGGING_DOCUSIGN_GET_DOCUMENT);
        EnvelopesApi.GetDocumentOptions options = envelopesApi.new GetDocumentOptions();
        if (DOCUSIGN_DOCUMENT_ID_CERTIFICATE.equalsIgnoreCase(documentId)) {
            options.setLanguage(LANGUAGE_OF_CERTIFICATE);
        } else if (DOCUSIGN_DOCUMENT_ID_COMBINED.equalsIgnoreCase(documentId)) {
            options.setCertificate(Boolean.FALSE.toString());
        }
        byte[] documentBytes = envelopesApi.getDocument(accountId, envelopeIdStr, documentId, options);
        reportDocuSignApiCallFinished(LOGGING_DOCUSIGN_GET_DOCUMENT, apiClient);

        return documentBytes;
    }

    public EnvelopeDocumentsResult apiListDocuments(String envelopeIdStr) throws ApiException {
        String accountId = docuSignApiClient.getAccountId();
        ApiClient apiClient = docuSignApiClient.getApiClient(docuSignConfig.getAdminUserId());
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        reportDocuSignApiCallStart(LOGGING_DOCUSIGN_LIST_DOCUMENTS);
        EnvelopeDocumentsResult documentsResult = envelopesApi.listDocuments(accountId, envelopeIdStr);
        reportDocuSignApiCallFinished(LOGGING_DOCUSIGN_LIST_DOCUMENTS, apiClient);

        return documentsResult;
    }

    public EnvelopeSummary apiCreateEnvelope(UUID apiUserId, EnvelopeDefinition envelopeDefinition) throws ApiException {
        String accountId = docuSignApiClient.getAccountId();
        ApiClient apiClient = docuSignApiClient.getApiClient(apiUserId);
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        reportDocuSignApiCallStart(LOGGING_DOCUSIGN_CREATE_ENVELOPE);
        EnvelopeSummary envelopeSummary = envelopesApi.createEnvelope(accountId, envelopeDefinition);
        reportDocuSignApiCallFinished(LOGGING_DOCUSIGN_CREATE_ENVELOPE, apiClient);

        return envelopeSummary;
    }

    public RecipientsUpdateSummary apiUpdateEnvelope(UUID apiUserId,
            String envelopeId,
            Recipients recipients,
            EnvelopeDefinition envelopeDefinition,
            List<String> additionalDocumentIds) throws ApiException {

        String accountId = docuSignApiClient.getAccountId();
        ApiClient apiClient = docuSignApiClient.getApiClient(apiUserId);

        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);
        reportDocuSignApiCallStart(LOGGING_DOCUSIGN_UPDATE_ENVELOPE);

        String status = envelopesApi.getEnvelope(accountId, envelopeId).getStatus();

        List<Document> documentsToUpdate = envelopeDefinition.getDocuments().stream()
                .filter(document -> !additionalDocumentIds.contains(document.getDocumentId()))
                .collect(Collectors.toList());

        List<String> idsToRemove = new ArrayList<>(additionalDocumentIds);

        EnvelopeDocumentsResult listDocuments = envelopesApi.listDocuments(accountId, envelopeId);
        idsToRemove.addAll(getRemovedDocuments(documentsToUpdate, listDocuments));

        List<Document> documentsToRemove = idsToRemove.stream().map(id -> {
            Document document = new Document();
            document.setDocumentId(id);
            return document;
        }).collect(Collectors.toList());

        updateDocuments(envelopeId, envelopeDefinition, accountId, envelopesApi, documentsToUpdate, documentsToRemove);

        RecipientsUpdateSummary summary = envelopesApi.updateRecipients(accountId, envelopeId, recipients);
        reportDocuSignApiCallFinished(LOGGING_DOCUSIGN_UPDATE_ENVELOPE, apiClient);
        status = envelopesApi.getEnvelope(accountId, envelopeId).getStatus();

        return summary;
    }

    public void apiVoidEnvelope(UUID apiUserId, String envelopeId, Envelope envelope) throws ApiException {
        String accountId = docuSignApiClient.getAccountId();
        ApiClient apiClient = docuSignApiClient.getApiClient(apiUserId);
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        reportDocuSignApiCallStart(LOGGING_DOCUSIGN_VOID_ENVELOPE);
        envelopesApi.update(accountId, envelopeId, envelope);
        reportDocuSignApiCallFinished(LOGGING_DOCUSIGN_VOID_ENVELOPE, apiClient);
    }

    public DigitalContractEmbeddedSendingBean getCorrectViewUrl(UUID apiUserId, String redirectUrl, String envelopeId) {
        String accountId = docuSignApiClient.getAccountId();
        ApiClient apiClient = docuSignApiClient.getApiClient(apiUserId);
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        CorrectViewRequest correctViewRequest = new CorrectViewRequest();
        correctViewRequest.setReturnUrl(redirectUrl);
        ViewUrl correctView = null;
        try {
            correctView = envelopesApi.createCorrectView(accountId, envelopeId, correctViewRequest);
            return DigitalContractEmbeddedSendingBean
                    .builder()
                    .embeddedUrl(correctView.getUrl())
                    .build();
        } catch (ApiException e) {
            log.error(e.getMessage(), e);

            throw new DocuSignApiException(e.getMessage(), e.getResponseBody());
        }
    }

    public DigitalContractEmbeddedSendingBean getEmbeddedSendingUrl(UUID apiUserId, String redirectUrl, String envelopeId) throws DocuSignApiException {
        String accountId = docuSignApiClient.getAccountId();
        ApiClient apiClient = docuSignApiClient.getApiClient(apiUserId);
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        ReturnUrlRequest returnUrlRequest = new ReturnUrlRequest();
        returnUrlRequest.setReturnUrl(redirectUrl);

        try {
            reportDocuSignApiCallStart(LOGGING_DOCUSIGN_CREATE_SENDER_VIEW);
            ViewUrl viewUrl = envelopesApi.createSenderView(accountId, envelopeId, returnUrlRequest);
            reportDocuSignApiCallFinished(LOGGING_DOCUSIGN_CREATE_SENDER_VIEW, apiClient);

            return DigitalContractEmbeddedSendingBean
                    .builder()
                    .embeddedUrl(viewUrl.getUrl())
                    .build();
        } catch (ApiException e) {
            log.error(e.getMessage(), e);

            throw new DocuSignApiException(e.getMessage(), e.getResponseBody());
        }
    }

    public void apiCreateTabs(String envelopeId, String recipientId, Tabs tabs) throws ApiException {
        String accountId = docuSignApiClient.getAccountId();
        ApiClient apiClient = docuSignApiClient.getApiClient(docuSignConfig.getAdminUserId());
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        reportDocuSignApiCallStart(LOGGING_DOCUSIGN_CREATE_TABS);
        envelopesApi.createTabs(accountId, envelopeId, recipientId, tabs);
        reportDocuSignApiCallFinished(LOGGING_DOCUSIGN_CREATE_TABS, apiClient);
    }

    public ViewUrl apiCreateRecipientView(String envelopeId, RecipientViewRequest recipientViewRequest) throws ApiException {
        String accountId = docuSignApiClient.getAccountId();
        ApiClient apiClient = docuSignApiClient.getApiClient(docuSignConfig.getAdminUserId());
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        reportDocuSignApiCallStart(LOGGING_DOCUSIGN_CREATE_RECIPIENT_VIEW);
        ViewUrl viewUrl = envelopesApi.createRecipientView(accountId, envelopeId, recipientViewRequest);
        reportDocuSignApiCallFinished(LOGGING_DOCUSIGN_CREATE_RECIPIENT_VIEW, apiClient);

        return viewUrl;
    }

    public void apiUpdateDocuments(String envelopeId, EnvelopeDefinition envelopeDefinition) throws ApiException {
        String accountId = docuSignApiClient.getAccountId();
        ApiClient apiClient = docuSignApiClient.getApiClient(docuSignConfig.getAdminUserId());
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        reportDocuSignApiCallStart(LOGGING_DOCUSIGN_UPDATE_DOCUMENTS);
        envelopesApi.updateDocuments(accountId, envelopeId, envelopeDefinition);
        reportDocuSignApiCallFinished(LOGGING_DOCUSIGN_UPDATE_DOCUMENTS, apiClient);
    }

    public Recipients apiListRecipientsFromTemplate(String templateId) throws ApiException {
        String accountId = docuSignApiClient.getAccountId();
        ApiClient apiClient = docuSignApiClient.getApiClient(docuSignConfig.getAdminUserId());
        TemplatesApi templatesApi = new TemplatesApi(apiClient);

        TemplatesApi.ListRecipientsOptions options = templatesApi.new ListRecipientsOptions();
        options.setIncludeTabs(Boolean.TRUE.toString());

        reportDocuSignApiCallStart(LOGGING_DOCUSIGN_LIST_RECIPIENTS_FROM_TEMPLATE);
        Recipients recipients = templatesApi.listRecipients(accountId, templateId, options);
        reportDocuSignApiCallFinished(LOGGING_DOCUSIGN_LIST_RECIPIENTS_FROM_TEMPLATE, apiClient);

        return recipients;
    }

    public Recipients apiListRecipientsFromEnvelope(String envelopeId) throws ApiException {
        String accountId = docuSignApiClient.getAccountId();
        ApiClient apiClient = docuSignApiClient.getApiClient(docuSignConfig.getAdminUserId());
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        reportDocuSignApiCallStart(LOGGING_DOCUSIGN_LIST_RECIPIENTS_FROM_ENVELOPE);
        Recipients recipients = envelopesApi.listRecipients(accountId, envelopeId);
        reportDocuSignApiCallFinished(LOGGING_DOCUSIGN_LIST_RECIPIENTS_FROM_ENVELOPE, apiClient);

        return recipients;
    }

    public void apiUpdateRecipientsDocumentVisibility(String envelopeId, DocumentVisibilityList documentVisibilityList) throws ApiException {
        String accountId = docuSignApiClient.getAccountId();
        ApiClient apiClient = docuSignApiClient.getApiClient(docuSignConfig.getAdminUserId());
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        reportDocuSignApiCallStart(LOGGING_DOCUSIGN_UPDATE_DOCUMENT_VISIBILITY);
        envelopesApi.updateRecipientsDocumentVisibility(accountId, envelopeId, documentVisibilityList);
        reportDocuSignApiCallFinished(LOGGING_DOCUSIGN_UPDATE_DOCUMENT_VISIBILITY, apiClient);
    }

    public Tabs apiListTabs(String envelopeId, String recipientId) throws ApiException {
        String accountId = docuSignApiClient.getAccountId();
        ApiClient apiClient = docuSignApiClient.getApiClient(docuSignConfig.getAdminUserId());
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        reportDocuSignApiCallStart(LOGGING_DOCUSIGN_LIST_TABS);
        Tabs tabs = envelopesApi.listTabs(accountId, envelopeId, recipientId);
        reportDocuSignApiCallFinished(LOGGING_DOCUSIGN_LIST_TABS, apiClient);

        return tabs;
    }

    public void apiListTabs(String envelopeId, String recipientId, Tabs updatedTabs) throws ApiException {
        String accountId = docuSignApiClient.getAccountId();
        ApiClient apiClient = docuSignApiClient.getApiClient(docuSignConfig.getAdminUserId());
        EnvelopesApi envelopesApi = new EnvelopesApi(apiClient);

        reportDocuSignApiCallStart(LOGGING_DOCUSIGN_UPDATE_TABS);
        envelopesApi.updateTabs(accountId, envelopeId, recipientId, updatedTabs);
        reportDocuSignApiCallFinished(LOGGING_DOCUSIGN_UPDATE_TABS, apiClient);
    }

    private List<String> getRemovedDocuments(List<Document> documentsToUpdate, EnvelopeDocumentsResult listDocuments) {
        return listDocuments.getEnvelopeDocuments().stream()
                .map(EnvelopeDocument::getDocumentId)
                .filter(documentId -> documentsToUpdate.stream()
                        .map(Document::getDocumentId).noneMatch(idToUpdate -> idToUpdate.equals(documentId))).collect(Collectors.toList());
    }

    private void updateDocuments(String envelopeId,
            EnvelopeDefinition envelopeDefinition,
            String accountId,
            EnvelopesApi envelopesApi,
            List<Document> documentsToUpdate,
            List<Document> documentsToRemove) throws ApiException {
        envelopeDefinition.setDocuments(documentsToRemove);

        envelopesApi.deleteDocuments(accountId, envelopeId, envelopeDefinition);

        envelopeDefinition.setDocuments(documentsToUpdate);

        envelopesApi.updateDocuments(accountId, envelopeId, envelopeDefinition);
    }

    private void reportDocuSignApiCallStart(String apiCallMethod) {
        log.info(LOGGING_DOCUSIGN_API_CALL_START, apiCallMethod);
    }

    private void reportDocuSignApiCallFinished(String apiCallMethod, ApiClient apiClient) {
        log.info(LOGGING_DOCUSIGN_API_CALL_FINISHED, apiCallMethod, apiClient.getResponseHeaders());
    }

}
