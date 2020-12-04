package de.immomio.constants.exceptions;

import lombok.Getter;

public class DocuSignApiException extends ImmomioRuntimeException {

    private static final long serialVersionUID = -1458723962397893057L;

    @Getter
    private String docuSignResponseBody;

    public DocuSignApiException() {
        super();
    }

    public DocuSignApiException(String message) {
        super(message);
    }

    public DocuSignApiException(String message, String docuSignResponseBody) {
        this.docuSignResponseBody = docuSignResponseBody;
    }

}
