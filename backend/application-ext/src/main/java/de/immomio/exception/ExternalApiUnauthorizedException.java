package de.immomio.exception;

import de.immomio.constants.exceptions.ImmomioRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Niklas Lindemann
 */
@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
public class ExternalApiUnauthorizedException extends ImmomioRuntimeException {
    private static final long serialVersionUID = 1163133015239356058L;


}
