package de.immomio.docusign.service;

import com.docusign.esign.model.Checkbox;
import com.docusign.esign.model.DateSigned;
import com.docusign.esign.model.SignHere;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.TabGroup;
import com.docusign.esign.model.Tabs;
import com.docusign.esign.model.Text;
import de.immomio.data.base.type.contract.signer.DigitalContractSignerType;
import de.immomio.data.landlord.bean.customer.settings.DigitalContractMappingField;
import de.immomio.data.landlord.bean.customer.settings.DigitalContractMappingFields;
import de.immomio.data.shared.entity.contract.DigitalContract;
import de.immomio.data.shared.entity.contract.signer.DigitalContractSigner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class DocuSignFieldMappingService {

    private static final String LABEL_PREFIX = "#";
    private static final String LABEL_EXTENSION_LANDLORD = "_vermieter_";
    private static final String LABEL_EXTENSION_TENANT = "_mieter_";
    private static final String CHECKBOX_GROUP_LABEL_PREFIX = "Checkbox Group ";
    private static final String SELECT_AT_LEAST = "SelectAtLeast";
    private static final String CONST_ONE = "1";
    private static final String TAB_GROUP_SCOPE = "envelope";
    private static final String ANCHOR_UNITS = "pixels";
    private static final String LABEL_SEPARATOR = "_";

    public void createSignatureTabs(Signer signer, DigitalContractSigner dcSigner, AtomicInteger landlordCount, AtomicInteger tenantCount) {
        try {
            DigitalContract digitalContract = dcSigner.getDigitalContract();
            DigitalContractMappingFields mappingFields = digitalContract.getCustomer().getCustomerSettings().getContractCustomerSettings().getMappingFields();

            int numberOfDocuments = digitalContract.getDocumentFiles().size();

            String labelExtension;
            if (dcSigner.getType() == DigitalContractSignerType.LANDLORD) {
                labelExtension = LABEL_EXTENSION_LANDLORD + landlordCount.getAndIncrement();
            } else {
                labelExtension = LABEL_EXTENSION_TENANT + tenantCount.getAndIncrement();
            }
            Tabs tabs = new Tabs();
            mappingFields.forEach(field -> createTab(signer, tabs, field, labelExtension, numberOfDocuments));

            signer.setTabs(tabs);
        } catch (Exception ex) {
            log.error("Error creating the automapping fields", ex);
        }
    }

    private void createTab(Signer signer, Tabs tabs, DigitalContractMappingField field, String labelExtension, int numberOfDocuments) {
        if (field == null) {
            return;
        }
        switch (field.getType()) {
            case SIGNATURE:
                addSignHereTab(tabs, field, labelExtension);
                break;
            case DATE:
                addDateSignedTab(tabs, field, labelExtension);
                break;
            case CHECKBOX:
                addCheckboxTab(signer, tabs, field, labelExtension, numberOfDocuments);
                break;
            default:
                addTextTab(tabs, field, labelExtension);
        }
    }

    private void addSignHereTab(Tabs tabs, DigitalContractMappingField field, String labelExtension) {
        String anchorString = LABEL_PREFIX + field.getLabel() + labelExtension;
        SignHere signHere = new SignHere();
        signHere.setTabLabel(anchorString);
        signHere.setAnchorString(anchorString);
        signHere.setAnchorUnits(ANCHOR_UNITS);
        signHere.setAnchorXOffset(field.getAnchorXOffset());
        signHere.setAnchorYOffset(field.getAnchorYOffset());

        tabs.addSignHereTabsItem(signHere);
    }

    private void addTextTab(Tabs tabs, DigitalContractMappingField field, String labelExtension) {
        String anchorString = LABEL_PREFIX + field.getLabel() + labelExtension;
        Text textTab = new Text();
        textTab.setTabLabel(anchorString);
        textTab.setAnchorString(anchorString);
        textTab.setAnchorUnits(ANCHOR_UNITS);
        textTab.setAnchorXOffset(field.getAnchorXOffset());
        textTab.setAnchorYOffset(field.getAnchorYOffset());
        textTab.setWidth(field.getWidth());
        textTab.setFont(field.getFont());
        textTab.setFontSize(field.getFontSize());
        if (field.getMandatory() != null) {
            textTab.setRequired(field.getMandatory().toString());
        }

        tabs.addTextTabsItem(textTab);
    }

    private void addDateSignedTab(Tabs tabs, DigitalContractMappingField field, String labelExtension) {
        String anchorString = LABEL_PREFIX + field.getLabel() + labelExtension;
        DateSigned dateSigned = new DateSigned();
        dateSigned.setTabLabel(anchorString);
        dateSigned.setAnchorString(anchorString);
        dateSigned.setAnchorUnits(ANCHOR_UNITS);
        dateSigned.setAnchorXOffset(field.getAnchorXOffset());
        dateSigned.setAnchorYOffset(field.getAnchorYOffset());
        dateSigned.setFont(field.getFont());
        dateSigned.setFontSize(field.getFontSize());

        tabs.addDateSignedTabsItem(dateSigned);
    }

    private void addCheckboxTab(Signer signer, Tabs tabs, DigitalContractMappingField field, String labelExtension, int numberOfDocuments) {
        String anchorString = LABEL_PREFIX + field.getLabel() + labelExtension;

        Checkbox checkbox = new Checkbox();
        checkbox.setRecipientId(signer.getRecipientId());
        checkbox.setTabLabel(anchorString);
        checkbox.setAnchorString(anchorString);
        checkbox.setAnchorUnits(ANCHOR_UNITS);
        checkbox.setAnchorXOffset(field.getAnchorXOffset());
        checkbox.setAnchorYOffset(field.getAnchorYOffset());

        tabs.addCheckboxTabsItem(checkbox);

        if (field.getMandatory() != null && field.getMandatory()) {
            List<String> groupLabels = new ArrayList<>();
            for (int docId = 1; docId <= numberOfDocuments; docId++) {
                String groupLabel = CHECKBOX_GROUP_LABEL_PREFIX + field.getLabel() + LABEL_SEPARATOR + docId + labelExtension;
                groupLabels.add(groupLabel);
                TabGroup tabGroup = new TabGroup();
                tabGroup.setGroupLabel(groupLabel);
                tabGroup.setGroupRule(SELECT_AT_LEAST);
                tabGroup.setMaximumAllowed(CONST_ONE);
                tabGroup.setMinimumRequired(CONST_ONE);
                tabGroup.setTabScope(TAB_GROUP_SCOPE);
                tabGroup.setDocumentId(String.valueOf(docId));
                tabGroup.setPageNumber(CONST_ONE);

                tabs.addTabGroupsItem(tabGroup);
            }
            checkbox.setTabGroupLabels(groupLabels);
        }
    }

}