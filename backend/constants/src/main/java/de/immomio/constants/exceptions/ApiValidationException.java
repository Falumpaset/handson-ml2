package de.immomio.constants.exceptions;

public class ApiValidationException extends ImmomioRuntimeException {

    private static final long serialVersionUID = -6262966943291229638L;

    public ApiValidationException() {
        super();
    }

    public ApiValidationException(String message) {
        super(message);
    }

}
