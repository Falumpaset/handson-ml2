package de.immomio.docusign.service.beans;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DigitalContractEmbeddedSendingBean {

    private UUID internalContractId;
    private String embeddedUrl;

}
