package de.immomio.constants.exceptions;

/**
 * @author Niklas Lindemann
 */
public class ImmomioTechnicalException extends Exception{
    private static final long serialVersionUID = -4287173728186613159L;

    public ImmomioTechnicalException(String message) {
        super(message);
    }

    public ImmomioTechnicalException(Throwable cause) {
        super(cause);
    }
}
