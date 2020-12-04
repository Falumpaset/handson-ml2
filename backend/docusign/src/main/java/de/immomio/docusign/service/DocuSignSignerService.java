package de.immomio.docusign.service;

import com.docusign.esign.client.ApiException;
import com.docusign.esign.model.DateSigned;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.DocumentVisibility;
import com.docusign.esign.model.DocumentVisibilityList;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.FullName;
import com.docusign.esign.model.RadioGroup;
import com.docusign.esign.model.RecipientSignatureProvider;
import com.docusign.esign.model.RecipientSignatureProviderOptions;
import com.docusign.esign.model.RecipientViewRequest;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.SignHere;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;
import com.docusign.esign.model.Text;
import com.docusign.esign.model.ViewUrl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.core.util.Base64;
import de.immomio.constants.exceptions.DocuSignApiException;
import de.immomio.data.base.type.contract.DigitalContractSignatureType;
import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.shared.bean.contract.DigitalContractSignerAdditionalDocumentIds;
import de.immomio.data.shared.bean.contract.DigitalContractSignerData;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import de.immomio.docusign.service.beans.DigitalContractEmbeddedSendingBean;
import de.immomio.docusign.service.beans.DocuSignAdditionalDocumentType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author Andreas Hansen.
 */
@Slf4j
@Service
public class DocuSignSignerService {

    private static final String RECIPIENT_VIEW_REQUEST_AUTHENTICATION_METHOD = "email";
    private static final String SIGNATURE_DATE_FORMAT_PATTERN = "dd.MM.yyyy";
    private static final String SIGNATURE_TIME_FORMAT_PATTERN = "HH:mm";
    private static final String TAB_FONT_COLOR_WHITE = "white";
    private static final String TAB_LABEL_INVISIBLE = "invisible";
    private static final String TEXTTAB_FLAT_VISITED = "FlatVisited";
    private static final String TEXTTAB_ORT_EINTRAGEN = "Ort eintragen";
    private static final String AES_SIGNATURE_PROVIDER_NAME = "UniversalSignaturePen_OpenTrust_Hash_TSP";
    private static final String RADIOTAB_FLAT_VISITED = "FlatVisited";
    private static final String RADIO_GOUP_NAME = "Group";
    private static final String TEXTTAB_ONSITE_SIGNER_HOST = "OnsiteSignerHost";
    private static final String TEXTTAB_ONSITE_SIGNER_NAME = "OnsiteSignerName";
    private static final String TEXTTAB_ONSITE_SIGNER_DATE = "OnsiteSignerDate";
    private static final String TEXTTAB_ONSITE_SIGNER_TIME = "OnsiteSignerTime";

    private final DocuSignSignerConfig docuSignSignerConfig;
    private final DocuSignFieldMappingService docuSignFieldMappingService;
    private final DocuSignApiService docuSignApiService;

    @Value("${timezone.europe}")
    private String timezone;

    @Autowired
    public DocuSignSignerService(
            DocuSignSignerConfig docuSignSignerConfig,
            DocuSignFieldMappingService docuSignFieldMappingService,
            DocuSignApiService docuSignApiService
    ) {
        this.docuSignSignerConfig = docuSignSignerConfig;
        this.docuSignFieldMappingService = docuSignFieldMappingService;
        this.docuSignApiService = docuSignApiService;
    }

    public Recipients createRecipients(
            DigitalContract digitalContract,
            List<DigitalContractSigner> digitalContractSigners
    ) {
        Recipients recipients = new Recipients();

        AtomicInteger landlordCount = new AtomicInteger(1);
        AtomicInteger tenantCount = new AtomicInteger(1);

        digitalContractSigners.forEach(dcSigner -> {
            int routingOrder;
            if (digitalContract.getSignatureType() != DigitalContractSignatureType.AES_OFFICE) {
                routingOrder = (dcSigner.getType() == digitalContract.getFirstSignerType()) ? 1 : 10;
            } else {
                routingOrder = (dcSigner.getType() == DigitalContractSignerType.TENANT) ? 1 : 10;
            }

            Signer signer = new Signer();
            DigitalContractSignerData signerData = dcSigner.getData();
            signer.setName(signerData.getFirstname() + " " + signerData.getLastname());
            signer.setEmail(signerData.getEmail());

            // the rolename has to be unique in the envelope, but is free to set, this is just a proposal
            signer.setRoleName(dcSigner.getType() + " " + dcSigner.getId());

            // the order in which the signers have to sign, it is possible to give identical numbers,
            // if the order is not relevant
            signer.setRoutingOrder(String.valueOf(routingOrder));

            // the recipient id has to be unique, but is defined in our service, so maybe we can use the signer_id
            signer.setRecipientId(String.valueOf(dcSigner.getId()));

            // the clientid must be set for embedded signing
            signer.setClientUserId(dcSigner.getDocuSignRecipientClientId().toString());

            docuSignFieldMappingService.createSignatureTabs(signer, dcSigner, landlordCount, tenantCount);

            setSignatureProvidersIfNeeded(dcSigner, signer);

            recipients.addSignersItem(signer);
        });

        return recipients;
    }

    public DigitalContractEmbeddedSendingBean getEmbeddedSigningUrl(DigitalContractSigner dcSigner, String redirectUrl) throws DocuSignApiException {
        RecipientViewRequest recipientViewRequest = getRecipientViewRequest(dcSigner, redirectUrl);

        try {
            ViewUrl viewUrl = docuSignApiService.apiCreateRecipientView(
                    dcSigner.getDigitalContract().getDocuSignEnvelopeId().toString(),
                    recipientViewRequest
            );

            return DigitalContractEmbeddedSendingBean
                    .builder()
                    .embeddedUrl(viewUrl.getUrl())
                    .build();
        } catch (ApiException e) {
            log.error(e.getMessage(), e);

            throw new DocuSignApiException(e.getMessage(), e.getResponseBody());
        }
    }

    public void addAdditionalDocuments(DigitalContract digitalContract, List<DigitalContractSigner> digitalContractSigners, String envelopeId) {
        try {
            EnvelopeDefinition envelopeDefinition = new EnvelopeDefinition();
            envelopeDefinition.setEnvelopeId(envelopeId);

            List<DigitalContractSigner> tenantSigners = digitalContractSigners
                    .stream()
                    .filter(dcSigner -> dcSigner.getType() == DigitalContractSignerType.TENANT)
                    .collect(Collectors.toList());

            if (digitalContract.getSignatureType() == DigitalContractSignatureType.AES_OFFICE) {
                Optional<DigitalContractSigner> onsiteHostSignerOpt = digitalContractSigners.stream().filter(DigitalContractSigner::isOnsiteHost).findFirst();
                createIdentityConfirmedDocuments(tenantSigners, envelopeDefinition);
                docuSignApiService.apiUpdateDocuments(envelopeId, envelopeDefinition);
                onsiteHostSignerOpt.ifPresent(onsiteHostSigner -> createIdentityConfirmationTabs(onsiteHostSigner, tenantSigners, envelopeId));
            } else {
                createViewingDateCombinedDocuments(tenantSigners, envelopeDefinition);
                docuSignApiService.apiUpdateDocuments(envelopeId, envelopeDefinition);
                createViewingDateTabs(digitalContractSigners, envelopeId);
            }
        } catch (ApiException e) {
            log.error(e.getMessage(), e);

            throw new DocuSignApiException(e.getMessage(), e.getResponseBody());
        }
    }

    public void updateViewingDateCombinedDocument(DigitalContractSigner digitalContractSigner, DocuSignAdditionalDocumentType documentType) throws DocuSignApiException {
        try {
            String envelopeId = digitalContractSigner.getDigitalContract().getDocuSignEnvelopeId().toString();
            String recipientId = String.valueOf(digitalContractSigner.getId());
            Tabs tabs = docuSignApiService.apiListTabs(envelopeId, recipientId);
            Tabs updatedTabs = new Tabs();
            if (documentType == DocuSignAdditionalDocumentType.VIEWING_DATE) {
                List<RadioGroup> radioGroups = tabs.getRadioGroupTabs();

                radioGroups.stream().map(RadioGroup::getRadios).flatMap(List::stream).forEach(radio -> {
                    if (RADIOTAB_FLAT_VISITED.equalsIgnoreCase(radio.getValue())) {
                        radio.setSelected(Boolean.TRUE.toString());
                    } else {
                        radio.setSelected(Boolean.FALSE.toString());
                    }
                });
                updatedTabs.setRadioGroupTabs(radioGroups);
            }

            List<Text> textTabs = tabs.getTextTabs();
            textTabs.forEach(textTab -> {
                if (documentType == DocuSignAdditionalDocumentType.VIEWING_DATE && textTab.getValue().contains(TEXTTAB_FLAT_VISITED)) {
                    textTab.setValue(getFlatVisitedStr(digitalContractSigner));
                    updatedTabs.addTextTabsItem(textTab);
                } else if (textTab.getValue().contains(TEXTTAB_ORT_EINTRAGEN)) {
                    textTab.setValue(getCity(digitalContractSigner));
                    updatedTabs.addTextTabsItem(textTab);
                }
            });
            docuSignApiService.apiListTabs(envelopeId, recipientId, updatedTabs);
        } catch (ApiException e) {
            log.error(e.getMessage(), e);

            throw new DocuSignApiException(e.getMessage(), e.getResponseBody());
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            throw new DocuSignApiException(e.getMessage());
        }
    }

    public void updateIdentityConfirmedDocument(DigitalContractSigner onsiteHostSigner, DigitalContractSigner signer) throws DocuSignApiException {
        try {
            String envelopeId = onsiteHostSigner.getDigitalContract().getDocuSignEnvelopeId().toString();
            String recipientId = String.valueOf(onsiteHostSigner.getId());
            Tabs tabs = docuSignApiService.apiListTabs(envelopeId, recipientId);
            Tabs updatedTabs = new Tabs();

            String docId = signer.getDocuSignAdditionalDocumentIds().getIdentityConfirmationDocId();
            List<Text> textTabs = tabs.getTextTabs();
            textTabs.forEach(textTab -> {
                if (textTab.getDocumentId().equals(docId)) {
                    if (textTab.getValue().contains(TEXTTAB_ONSITE_SIGNER_DATE)) {
                        textTab.setValue(getOnsiteSignerDate(signer));
                        updatedTabs.addTextTabsItem(textTab);
                    } else if (textTab.getValue().contains(TEXTTAB_ONSITE_SIGNER_TIME)) {
                        textTab.setValue(getOnsiteSignerTime(signer));
                        updatedTabs.addTextTabsItem(textTab);
                    }
                }
            });
            docuSignApiService.apiListTabs(envelopeId, recipientId, updatedTabs);
        } catch (ApiException e) {
            log.error(e.getMessage(), e);

            throw new DocuSignApiException(e.getMessage(), e.getResponseBody());
        } catch (Exception e) {
            log.error(e.getMessage(), e);

            throw new DocuSignApiException(e.getMessage());
        }
    }


    private void createViewingDateCombinedDocuments(List<DigitalContractSigner> tenantSigners, EnvelopeDefinition envelopeDefinition) {
        AtomicInteger additionalDocumentId = new AtomicInteger(100);
        tenantSigners.forEach(dcSigner -> {
            String docId = String.valueOf(additionalDocumentId.getAndIncrement());
            createViewingDateCombinedDocument(envelopeDefinition, docId);
            dcSigner.setDocuSignAdditionalDocumentIds(DigitalContractSignerAdditionalDocumentIds.builder().viewingDateCombinedDocId(docId).build());
        });
    }

    private void createIdentityConfirmedDocuments(List<DigitalContractSigner> tenantSigners, EnvelopeDefinition envelopeDefinition) {
        AtomicInteger additionalDocumentId = new AtomicInteger(100);
        tenantSigners.forEach(dcSigner -> {
            String docId = String.valueOf(additionalDocumentId.getAndIncrement());
            createIdentityConfirmedDocument(envelopeDefinition, docId);
            dcSigner.setDocuSignAdditionalDocumentIds(DigitalContractSignerAdditionalDocumentIds.builder().identityConfirmationDocId(docId).build());
        });
    }

    private void createViewingDateTabs(List<DigitalContractSigner> dcSigners, String envelopeId) {
        try {
            String templateId = docuSignSignerConfig.getDocumentViewingDateCombinedDocuSignTemplateId();
            Tabs viewingDateTabsFromTemplate = getTabsFromTemplate(templateId);
            if (viewingDateTabsFromTemplate != null) {
                dcSigners.forEach(dcSigner -> {
                    if (dcSigner.getType() == DigitalContractSignerType.TENANT) {
                        String recipientId = String.valueOf(dcSigner.getId());
                        DigitalContractSignerAdditionalDocumentIds documentIds = dcSigner.getDocuSignAdditionalDocumentIds();
                        String docId = (documentIds != null) ? documentIds.getViewingDateCombinedDocId() : "";
                        createTabs(recipientId, docId, envelopeId, viewingDateTabsFromTemplate, null);
                    }
                });
            }
        } catch (Exception ex) {
            log.error("Error creating the fields for additional document.", ex);
        }
    }

    private void createIdentityConfirmationTabs(
            DigitalContractSigner onsiteHostSigner,
            List<DigitalContractSigner> tenantSigners,
            String envelopeId) {
        try {
            String templateId = docuSignSignerConfig.getDocumentIdentityConfirmationDocuSignTemplateId();
            Tabs identityConfirmationTabsFromTemplate = getTabsFromTemplate(templateId);
            if (identityConfirmationTabsFromTemplate != null) {
                String recipientId = String.valueOf(onsiteHostSigner.getId());
                tenantSigners.forEach(dcSigner -> {
                    DigitalContractSignerAdditionalDocumentIds documentIds = dcSigner.getDocuSignAdditionalDocumentIds();
                    String docId = (documentIds != null) ? documentIds.getIdentityConfirmationDocId() : "";
                    Map<String, String> tabValues = new HashMap<>();
                    tabValues.put(TEXTTAB_ONSITE_SIGNER_HOST, onsiteHostSigner.getData().getFirstname() + " " + onsiteHostSigner.getData().getLastname());
                    tabValues.put(TEXTTAB_ONSITE_SIGNER_NAME, dcSigner.getData().getFirstname() + " " + dcSigner.getData().getLastname());
                    createTabs(recipientId, docId, envelopeId, identityConfirmationTabsFromTemplate, tabValues);
                });
            }
        } catch (Exception ex) {
            log.error("Error creating the fields for additional document.", ex);
        }
    }

    private void createTabs(String recipientId, String docId, String envelopeId, Tabs tabs, Map<String, String> tabValues) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Tabs deepCopyTabs = objectMapper.readValue(objectMapper.writeValueAsString(tabs), Tabs.class);
            if (!StringUtils.isEmpty(docId)) {
                createSignatureFields(recipientId, envelopeId, docId, deepCopyTabs, tabValues);
                setDocumentVisibility(recipientId, envelopeId, docId);
            }
        } catch (Exception ex) {
            log.error("Error creating the fields for additional document.", ex);
        }
    }

    private String getFlatVisitedStr(DigitalContractSigner digitalContractSigner) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(SIGNATURE_DATE_FORMAT_PATTERN);

        Date flatVisited = digitalContractSigner.getFlatVisited();
        return (flatVisited != null) ? formatter.format(getZonedDate(flatVisited)) : null;
    }

    private LocalDateTime getZonedDate(Date date) {
        return date.toInstant().atZone(ZoneId.of(timezone)).toLocalDateTime();
    }

    private String getCity(DigitalContractSigner digitalContractSigner) {
        if (digitalContractSigner.getData() != null
                && digitalContractSigner.getData().getAddress() != null
                && digitalContractSigner.getData().getAddress().getCity() != null) {
            return digitalContractSigner.getData().getAddress().getCity();
        }

        return "";
    }

    private String getOnsiteSignerDate(DigitalContractSigner digitalContractSigner) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(SIGNATURE_DATE_FORMAT_PATTERN);

        return (digitalContractSigner.getOnsiteDataVerified() != null) ? formatter.format(getZonedDate(digitalContractSigner.getOnsiteDataVerified())) : null;
    }

    private String getOnsiteSignerTime(DigitalContractSigner digitalContractSigner) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(SIGNATURE_TIME_FORMAT_PATTERN);

        return (digitalContractSigner.getOnsiteDataVerified() != null) ? formatter.format(getZonedDate(digitalContractSigner.getOnsiteDataVerified())) : null;
    }

    private RecipientViewRequest getRecipientViewRequest(DigitalContractSigner dcSigner, String redirectUrl) {
        RecipientViewRequest recipientViewRequest = new RecipientViewRequest();
        recipientViewRequest.setReturnUrl(redirectUrl);
        recipientViewRequest.setEmail(dcSigner.getData().getEmail());
        recipientViewRequest.setRecipientId(String.valueOf(dcSigner.getId()));
        recipientViewRequest.setUserName(dcSigner.getData().getFirstname() + " " + dcSigner.getData().getLastname());
        recipientViewRequest.setAuthenticationMethod(RECIPIENT_VIEW_REQUEST_AUTHENTICATION_METHOD);
        recipientViewRequest.setClientUserId(dcSigner.getDocuSignRecipientClientId().toString());

        return recipientViewRequest;
    }

    private void createViewingDateCombinedDocument(EnvelopeDefinition envelopeDefinition, String docId) {
        try {
            byte[] documentBytes = IOUtils.toByteArray(
                    getClass().getResourceAsStream(docuSignSignerConfig.getDocumentViewingDateCombinedPath()));
            Document doc = new Document();
            doc.setDocumentBase64(new String(Base64.encode(documentBytes)));
            doc.setName(docuSignSignerConfig.getDocumentViewingDateCombinedName());
            doc.setFileExtension(docuSignSignerConfig.getDocumentViewingDateCombinedExtension());
            doc.setDocumentId(docId);
            envelopeDefinition.addDocumentsItem(doc);
        } catch (Exception ex) {
            log.error("error creating additional document. ", ex);
        }
    }

    private void createIdentityConfirmedDocument(EnvelopeDefinition envelopeDefinition, String docId) {
        try {
            byte[] documentBytes = IOUtils.toByteArray(
                    getClass().getResourceAsStream(docuSignSignerConfig.getDocumentIdentityConfirmationPath()));
            Document doc = new Document();
            doc.setDocumentBase64(new String(Base64.encode(documentBytes)));
            doc.setName(docuSignSignerConfig.getDocumentIdentityConfirmationName());
            doc.setFileExtension(docuSignSignerConfig.getDocumentIdentityConfirmationExtension());
            doc.setDocumentId(docId);
            envelopeDefinition.addDocumentsItem(doc);
        } catch (Exception ex) {
            log.error("error creating additional document. ", ex);
        }
    }

    private Tabs getTabsFromTemplate(String templateId) throws ApiException {
        // get the predefined tabs from the first recipient of the template.
        log.info("TEMPLATE " + templateId);

        Recipients recipients = docuSignApiService.apiListRecipientsFromTemplate(templateId);

        Optional<Signer> signer = recipients.getSigners().stream().findFirst();
        if (signer.isPresent()) {
            return signer.get().getTabs();
        } else {
            log.error("No recipient and therefor nor tabs in the template {} found.", templateId);

            return null;
        }
    }

    private void createSignatureFields(
            String recipientId,
            String envelopeId,
            String docId,
            Tabs tabsFromTemplate,
            Map<String, String> tabValues
    ) throws ApiException {
        Tabs signerTabs = new Tabs();
        addRadioGroupTabs(recipientId, docId, signerTabs, tabsFromTemplate.getRadioGroupTabs());
        addTextTabs(recipientId, docId, signerTabs, tabsFromTemplate.getTextTabs(), tabValues);
        addDateSignedTabs(recipientId, docId, signerTabs, tabsFromTemplate.getDateSignedTabs());
        addSignHereTabs(recipientId, docId, signerTabs, tabsFromTemplate.getSignHereTabs());
        addFullNameTabs(recipientId, docId, signerTabs, tabsFromTemplate.getFullNameTabs());

        docuSignApiService.apiCreateTabs(envelopeId, recipientId, signerTabs);
    }

    /* because the Tab-Classes in DocuSign (Text, SignHere, DateSigned) don't implement any interface, it
    is not possible to merge the following add-methods to one method with generics!
     */
    private void addRadioGroupTabs(String recipientId, String docId, Tabs signerTabs, List<RadioGroup> tabs) {
        tabs.forEach(radioGroup -> {
            radioGroup.setGroupName(RADIO_GOUP_NAME + docId);
            radioGroup.setDocumentId(docId);
            radioGroup.setRecipientId(recipientId);

            signerTabs.addRadioGroupTabsItem(radioGroup);
        });
    }

    private void addTextTabs(String recipientId, String docId, Tabs signerTabs, List<Text> tabs, Map<String, String> tabValues) {
        tabs.forEach(textTab -> {
            if (!StringUtils.isEmpty(textTab.getConditionalParentLabel())) {
                textTab.setConditionalParentLabel(RADIO_GOUP_NAME + docId);
            }
            textTab.setTabLabel(textTab.getTabLabel() + " " + docId);
            textTab.setDocumentId(docId);
            textTab.setRecipientId(recipientId);
            if (tabValues != null) {
                tabValues.forEach((key, val) -> {
                    if (textTab.getValue().equalsIgnoreCase(key)) {
                        textTab.setValue(val);
                    }
                });
            }

            signerTabs.addTextTabsItem(textTab);
        });
    }

    private void addDateSignedTabs(
            String recipientId,
            String docId,
            Tabs signerTabs,
            List<DateSigned> tabs
    ) {
        tabs.forEach(dateSignedTab -> {
            dateSignedTab.setTabLabel(dateSignedTab.getTabLabel() + " " + docId);
            dateSignedTab.setDocumentId(docId);
            dateSignedTab.setRecipientId(recipientId);

            signerTabs.addDateSignedTabsItem(dateSignedTab);
        });
    }

    private void addSignHereTabs(String recipientId, String docId, Tabs signerTabs, List<SignHere> tabs) {
        tabs.forEach(signeHereTab -> {
            signeHereTab.setTabLabel(signeHereTab.getTabLabel() + " " + docId);
            signeHereTab.setDocumentId(docId);
            signeHereTab.setRecipientId(recipientId);

            signerTabs.addSignHereTabsItem(signeHereTab);
        });
    }

    private void addFullNameTabs(String recipientId, String docId, Tabs signerTabs, List<FullName> tabs) {
        tabs.forEach(fullName -> {
            fullName.setTabLabel(fullName.getTabLabel() + " " + docId);
            fullName.setDocumentId(docId);
            fullName.setRecipientId(recipientId);

            signerTabs.addFullNameTabsItem(fullName);
        });
    }

    private void setDocumentVisibility(String recipientId, String envelopeId, String additionalDocumentId) throws ApiException {
        DocumentVisibilityList documentVisibilityList = new DocumentVisibilityList();
        Recipients recipients = docuSignApiService.apiListRecipientsFromEnvelope(envelopeId);

        recipients.getSigners().forEach(signer -> {
            DocumentVisibility documentVisibility = new DocumentVisibility();
            documentVisibility.setDocumentId(additionalDocumentId);
            documentVisibility.setRecipientId(signer.getRecipientId());
            // additional document is visible for the tenant who shall sign this document and the landlord
            if (signer.getRecipientId().equals(recipientId)) {
                documentVisibility.setVisible(Boolean.TRUE.toString());
            } else if (signer.getRoleName().contains(DigitalContractSignerType.LANDLORD.toString())) {
                documentVisibility.setVisible(Boolean.TRUE.toString());
                createInvisibleTabForLandlord(recipientId, envelopeId, additionalDocumentId, signer);
            } else {
                documentVisibility.setVisible(Boolean.FALSE.toString());
            }

            documentVisibilityList.addDocumentVisibilityItem(documentVisibility);
        });
        docuSignApiService.apiUpdateRecipientsDocumentVisibility(envelopeId, documentVisibilityList);
    }

    private void createInvisibleTabForLandlord(
            String recipientId,
            String envelopeId,
            String additionalDocumentId,
            Signer signer
    ) {
        Text textTab = new Text();
        textTab.setTabLabel(TAB_LABEL_INVISIBLE + "_" + additionalDocumentId + "_" + recipientId);
        textTab.setDocumentId(additionalDocumentId);
        textTab.setRecipientId(signer.getRecipientId());
        textTab.setPageNumber(BigDecimal.ONE.toString());
        textTab.setXPosition(BigDecimal.ONE.toString());
        textTab.setYPosition(BigDecimal.ONE.toString());
        textTab.setWidth(BigDecimal.ONE.toString());
        textTab.setRequired(Boolean.FALSE.toString());
        textTab.setLocked(Boolean.TRUE.toString());
        textTab.setFontColor(TAB_FONT_COLOR_WHITE);

        Tabs tabs = new Tabs();
        tabs.setTextTabs(Collections.singletonList(textTab));
        try {
            docuSignApiService.apiCreateTabs(envelopeId, signer.getRecipientId(), tabs);
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    private void setSignatureProvidersIfNeeded(DigitalContractSigner dcSigner, Signer signer) {
        DigitalContract digitalContract = dcSigner.getDigitalContract();
        if (dcSigner.getType() == DigitalContractSignerType.TENANT
                && (digitalContract.getSignatureType() == DigitalContractSignatureType.AES_MAIL
                || digitalContract.getSignatureType() == DigitalContractSignatureType.AES_OFFICE)) {
            RecipientSignatureProvider aesProvider = new RecipientSignatureProvider();
            aesProvider.setSignatureProviderName(AES_SIGNATURE_PROVIDER_NAME);
            RecipientSignatureProviderOptions options = new RecipientSignatureProviderOptions();

            options.setOneTimePassword(dcSigner.getAesVerificationData().getAesCode());
            aesProvider.setSignatureProviderOptions(options);

            List<RecipientSignatureProvider> signatureProviders = List.of(aesProvider);
            signer.setRecipientSignatureProviders(signatureProviders);
        }
    }

}
