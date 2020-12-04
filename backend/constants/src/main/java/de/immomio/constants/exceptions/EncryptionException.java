package de.immomio.constants.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class EncryptionException extends Exception {

    private static final long serialVersionUID = 3895903340126310026L;

    public EncryptionException() {
        super();
    }

    public EncryptionException(String message) {
        super(message);
    }

    public EncryptionException(Throwable cause) {
        super(cause);
    }

    public EncryptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
