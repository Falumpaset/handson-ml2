package de.immomio.beans.shared.contract.tenant;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DigitalContractTenantWorkflowBean {

    private DigitalContractTenantWorkflowState workflowState;
    private UUID internalContractId;
    private String embeddedUrl;

}
