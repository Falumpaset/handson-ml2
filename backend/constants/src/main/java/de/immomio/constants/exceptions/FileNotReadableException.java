package de.immomio.constants.exceptions;

/**
 * @author Maik Kingma
 */

public class FileNotReadableException extends Exception {
    private static final long serialVersionUID = 735453725801548599L;

    public FileNotReadableException(String message) {
        super(message);
    }
}
