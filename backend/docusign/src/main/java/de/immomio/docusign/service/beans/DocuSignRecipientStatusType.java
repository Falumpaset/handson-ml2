package de.immomio.docusign.service.beans;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum DocuSignRecipientStatusType {

    SIGNER_STATUS_CREATED("created"),
    SIGNER_STATUS_SENT("sent"),
    SIGNER_STATUS_SIGNED("signed"),
    SIGNER_STATUS_DECLINED("declined"),
    SIGNER_STATUS_FAXPENDING("faxPending"),
    SIGNER_STATUS_AUTORESPONDED("autoResponded"),
    SIGNER_STATUS_DELIVERED("delivered"),
    SIGNER_STATUS_VOIDED("voided"),
    SIGNER_STATUS_COMPLETED("completed"),
    SIGNER_STATUS_CORRECT("correct");

    private String statusValue;

    public static DocuSignRecipientStatusType findByStatusValue(final String statusValue) {
        return Arrays.stream(values())
                .filter(value -> value.statusValue.equalsIgnoreCase(statusValue))
                .findFirst()
                .orElse(null);
    }
}
