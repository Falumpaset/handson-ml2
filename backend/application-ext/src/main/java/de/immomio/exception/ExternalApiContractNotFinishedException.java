package de.immomio.exception;

import de.immomio.constants.exceptions.ImmomioRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.LOCKED)
public class ExternalApiContractNotFinishedException extends ImmomioRuntimeException {

    private static final long serialVersionUID = -849841687012511473L;

    public ExternalApiContractNotFinishedException() {
        super();
    }

    public ExternalApiContractNotFinishedException(String message) {
        super(message);
    }

}
