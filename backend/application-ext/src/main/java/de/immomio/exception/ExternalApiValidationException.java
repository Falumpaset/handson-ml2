package de.immomio.exception;

import de.immomio.constants.exceptions.ImmomioRuntimeException;

public class ExternalApiValidationException extends ImmomioRuntimeException {

    private static final long serialVersionUID = 5413853230400409849L;

    public ExternalApiValidationException() {
        super();
    }

    public ExternalApiValidationException(String message) {
        super(message);
    }

}
