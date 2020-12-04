package de.immomio.constants.exceptions;

public class BadRequestException extends ImmomioRuntimeException {

    private static final long serialVersionUID = -6997935379414969772L;

    public BadRequestException() {
        super();
    }

    public BadRequestException(String message) {
        super(message);
    }

}
