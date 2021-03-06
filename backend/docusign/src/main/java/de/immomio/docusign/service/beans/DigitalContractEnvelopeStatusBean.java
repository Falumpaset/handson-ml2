package de.immomio.docusign.service.beans;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class DigitalContractEnvelopeStatusBean {

    private UUID envelopeId;
    private DocuSignEnvelopeStatusType status;
    private List<DigitalContractSignerStatusBean> signers;

}
