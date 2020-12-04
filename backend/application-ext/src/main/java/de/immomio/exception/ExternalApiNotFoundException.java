package de.immomio.exception;

import de.immomio.constants.exceptions.ImmomioRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ExternalApiNotFoundException extends ImmomioRuntimeException {

    private static final long serialVersionUID = 6494753380446912155L;

    public ExternalApiNotFoundException() {
        super();
    }

    public ExternalApiNotFoundException(String message) {
        super(message);
    }

}
