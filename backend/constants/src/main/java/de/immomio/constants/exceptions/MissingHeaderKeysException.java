package de.immomio.constants.exceptions;

/**
 * @author Maik Kingma
 */

public class MissingHeaderKeysException extends Exception {

    private static final long serialVersionUID = -6608520140783975103L;

    public MissingHeaderKeysException(String message) {
        super(message);
    }
}
