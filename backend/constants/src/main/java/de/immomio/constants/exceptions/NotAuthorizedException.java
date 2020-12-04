package de.immomio.constants.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Niklas Lindemann
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class NotAuthorizedException extends Exception {
    private static final long serialVersionUID = 6277690143984522793L;

    public NotAuthorizedException(String message) {
        super(message);
    }
}
