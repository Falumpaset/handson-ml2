package de.immomio.docusign.service.beans;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DigitalContractSignerStatusBean {

    private Long recipientId;
    private DocuSignRecipientStatusType status;

}
