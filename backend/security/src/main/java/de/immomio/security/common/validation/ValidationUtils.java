/**
 *
 */
package de.immomio.security.common.validation;

import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Johannes Hiemer.
 */
public final class ValidationUtils {

    private ValidationUtils() {
    }

    /**
     * @param failures
     * @return
     */
    public static <T> Map<String, String> getErrorMessages(Set<ConstraintViolation<T>> failures) {
        Map<String, String> failureMessages = new HashMap<>();
        for (ConstraintViolation<T> failure : failures) {
            failureMessages.put(failure.getPropertyPath().toString(), failure.getMessage());
        }
        return failureMessages;
    }

    /**
     * @param target
     * @param name
     * @return
     */
    public static Errors errors(Object target, String name) {
        return new BeanPropertyBindingResult(target, name);
    }

    /**
     * @param target
     * @return
     */
    public static Errors errors(Object target) {
        return new BeanPropertyBindingResult(target, target.getClass().getSimpleName());
    }

    /**
     * @param errors
     * @param field
     * @param errorCode
     */
    public static void rejectBlank(Errors errors, String field, String errorCode) {
        org.springframework.validation.ValidationUtils.rejectIfEmptyOrWhitespace(errors, field, errorCode);
    }

    /**
     * @param entity
     * @param errors
     * @param request
     * @return
     */
    public static ValidationEntity resolveResponse(String entity, Errors errors, HttpServletRequest request) {
        ValidationEntity validationEntity = new ValidationEntity();
        RequestContext requestContext = new RequestContext(request);
        for (FieldError error : errors.getFieldErrors()) {
            String message = requestContext.getMessage(error.getCode(), error.getArguments());
            validationEntity.getErrors().add(new ValidationEntityItem(entity, message, error.getField()));
        }

        return validationEntity;
    }

    /**
     * @param entity
     * @param errors
     * @return
     */
    public static ValidationEntity resolveResponse(String entity, Errors errors) {
        ValidationEntity validationEntity = new ValidationEntity();
        for (FieldError error : errors.getFieldErrors()) {
            validationEntity.getErrors().add(new ValidationEntityItem(entity, error.getCode(), error.getField()));
        }

        return validationEntity;
    }
}