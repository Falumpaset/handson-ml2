package de.immomio.docusign.service.beans;

import lombok.AllArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
public enum DocuSignEnvelopeStatusType {

    ENVELOPE_STATUS_CREATED("created"),
    ENVELOPE_STATUS_SENT("sent"),
    ENVELOPE_STATUS_DELIVERED("delivered"),
    ENVELOPE_STATUS_VOIDED("voided"),
    ENVELOPE_STATUS_COMPLETED("completed");

    private String statusValue;

    public static DocuSignEnvelopeStatusType findByStatusValue(final String statusValue) {
        return Arrays.stream(values())
                .filter(value -> value.statusValue.equalsIgnoreCase(statusValue))
                .findFirst()
                .orElse(null);
    }
}
