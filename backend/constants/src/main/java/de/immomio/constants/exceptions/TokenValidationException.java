package de.immomio.constants.exceptions;

public class TokenValidationException extends ImmomioRuntimeException {

    private static final long serialVersionUID = 7524837872063804853L;

    public TokenValidationException() {
        super();
    }

    public TokenValidationException(String message) {
        super(message);
    }

}
