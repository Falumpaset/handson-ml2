package de.immomio.constants.exceptions;

/**
 * @author Maik Kingma
 */

public class UserNotFoundException extends Exception {
    private static final long serialVersionUID = 3230133328411145315L;

    public UserNotFoundException(String message) {
        super(message);
    }
}
