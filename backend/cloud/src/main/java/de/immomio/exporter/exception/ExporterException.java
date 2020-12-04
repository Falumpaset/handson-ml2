/**
 *
 */
package de.immomio.exporter.exception;

/**
 * @author Johannes Hiemer.
 */
public class ExporterException extends Exception {

    private static final long serialVersionUID = -3214864644738230811L;

    public ExporterException(Throwable t) {
        super(t);
    }

    public ExporterException(String message, Throwable t) {
        super(message, t);
    }

    public ExporterException(String message) {
        super(message);
    }
}
