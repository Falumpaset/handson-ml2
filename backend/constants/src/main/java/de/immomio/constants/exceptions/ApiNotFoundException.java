package de.immomio.constants.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Fabian Beck
 */

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ApiNotFoundException extends ImmomioRuntimeException{

    private static final long serialVersionUID = -4198885664934690436L;

    public ApiNotFoundException() {
        super();
    }

    public ApiNotFoundException(String message) {
        super(message);
    }
}
