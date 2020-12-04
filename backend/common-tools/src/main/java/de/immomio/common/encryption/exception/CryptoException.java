/**
 *
 */
package de.immomio.common.encryption.exception;

/**
 * @author Johannes Hiemer.
 */
public class CryptoException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public CryptoException(String message, Exception cause) {
        super(message, cause);
    }

}
