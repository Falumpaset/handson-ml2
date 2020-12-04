package de.immomio.constants.exceptions;

/**
 * @author Niklas Lindemann
 */
public class SendGridException extends RuntimeException {

    private static final long serialVersionUID = -6422161925160976526L;

    public SendGridException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendGridException(String message) {
        super(message);
    }
}
