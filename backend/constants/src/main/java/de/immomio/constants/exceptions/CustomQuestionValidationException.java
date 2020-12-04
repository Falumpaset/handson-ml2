package de.immomio.constants.exceptions;

/**
 * @author Maik Kingma
 */

public class CustomQuestionValidationException extends Throwable {
    private static final long serialVersionUID = 7065184760305033466L;

    public CustomQuestionValidationException(String message) {
        super(message);
    }
}
