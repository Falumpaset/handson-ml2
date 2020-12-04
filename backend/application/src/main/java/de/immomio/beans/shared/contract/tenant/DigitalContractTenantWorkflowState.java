package de.immomio.beans.shared.contract.tenant;

public enum DigitalContractTenantWorkflowState {
    EMBEDDED_SIGNING,
    AES_CHECK,
    AES_CODE_OK,
    AES_CODE_RETRY,
    AES_CODE_FAILED
}
