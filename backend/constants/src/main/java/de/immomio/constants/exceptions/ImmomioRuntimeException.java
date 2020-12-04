package de.immomio.constants.exceptions;

public class ImmomioRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 2325633834118728783L;

    public ImmomioRuntimeException() {
        super();
    }

    public ImmomioRuntimeException(String message) {
        super(message);
    }

    public ImmomioRuntimeException(Throwable cause) {
        super(cause);
    }

    public ImmomioRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
