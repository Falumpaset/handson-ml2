package de.immomio.constants.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;

/**
 * @author Niklas Lindemann
 */

public class BasketCheckoutException extends Exception {
    private static final long serialVersionUID = -1612314761400254722L;

    @Getter
    private ObjectError objectError;

    @Getter
    private HttpStatus status;

    public BasketCheckoutException(ObjectError objectError, HttpStatus status) {
        this.objectError = objectError;
        this.status = status;
    }

    public BasketCheckoutException(HttpStatus status) {
        this.status = status;
    }
}
