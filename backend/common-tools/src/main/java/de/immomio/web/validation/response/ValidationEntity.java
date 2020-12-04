/**
 *
 */
package de.immomio.web.validation.response;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Johannes Hiemer, immomio.
 */
public class ValidationEntity {

    private List<ValidationEntityItem> errors = new ArrayList<>();

    public List<ValidationEntityItem> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationEntityItem> errors) {
        this.errors = errors;
    }

    public boolean hasErrors() {
        return this.errors.size() > 0;
    }

}