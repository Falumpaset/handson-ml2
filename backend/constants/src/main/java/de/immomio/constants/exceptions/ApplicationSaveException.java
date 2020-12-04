package de.immomio.constants.exceptions;

/**
 * @author Maik Kingma
 */

public class ApplicationSaveException extends Exception {

    private static final long serialVersionUID = -7376754594397739813L;

    public ApplicationSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
