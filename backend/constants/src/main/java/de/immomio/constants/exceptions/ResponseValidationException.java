package de.immomio.constants.exceptions;

/**
 * @author Maik Kingma
 */

public class ResponseValidationException extends Throwable {

    private static final long serialVersionUID = 5336781260988235017L;

    public ResponseValidationException(String message) {
        super(message);
    }
}
