package de.immomio.web.validation;

import de.immomio.web.validation.response.ValidationEntity;
import de.immomio.web.validation.response.ValidationEntityItem;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Johannes Hiemer, cloudscale.
 */
public final class ValidationUtils {

    private static final Pattern IP_SKELETON_PATTERN = Pattern.compile(
            "\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b");

    private static Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$",
            Pattern.CASE_INSENSITIVE);

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
     * @param errors
     * @param field
     * @param errorCode
     */
    public static void rejectNull(Errors errors, String field, String errorCode) {
        Object valueOne = errors.getFieldValue(field);
        if (valueOne == null) {
            errors.rejectValue(field, errorCode);
        }
    }

    /**
     * @param errors
     * @param fieldOne
     * @param fieldTwo
     * @param errorCode
     */
    public static void rejectNotEquals(Errors errors, String fieldOne, String fieldTwo, String errorCode) {
        String valueOne = (String) errors.getFieldValue(fieldOne);
        String valueTwo = (String) errors.getFieldValue(fieldTwo);
        if (valueOne != null && valueTwo != null && valueOne.compareTo(valueTwo) != 0) {
            errors.rejectValue(fieldOne, errorCode);
        }
    }

    /**
     * @param errors
     * @param field
     * @param errorCode
     */
    public static void rejectCustom(Errors errors, String field, String errorCode) {
        errors.rejectValue(field, errorCode);
    }

    /**
     * @param maxLength
     * @param errors
     * @param field
     * @param errorCode
     */
    public static void rejectMaxLength(int maxLength, Errors errors, String field, String errorCode) {
        String value = (String) errors.getFieldValue(field);
        if (value != null && value.trim().length() > maxLength) {
            errors.rejectValue(field, errorCode, new Object[]{maxLength}, "");
        }
    }

    /**
     * @param minLength
     * @param errors
     * @param field
     * @param errorCode
     */
    public static void rejectMinLength(int minLength, Errors errors, String field, String errorCode) {
        String value = (String) errors.getFieldValue(field);
        if (value != null && value.trim().length() < minLength) {
            errors.rejectValue(field, errorCode, new Object[]{minLength}, "");
        }
    }

    /**
     * @param minLength
     * @param maxLength
     * @param errors
     * @param field
     * @param errorCode
     */
    public static void rejectLength(int minLength, int maxLength, Errors errors, String field, String errorCode) {
        String value = (String) errors.getFieldValue(field);
        if (value != null) {
            int valueLength = value.trim().length();
            if (valueLength < minLength || valueLength > maxLength) {
                errors.rejectValue(field, errorCode, new Object[]{minLength, maxLength}, "");
            }
        }
    }

    /**
     * @param errors
     * @param field
     * @param errorCode
     */
    public static void rejectInvalidSymbols(Errors errors, String field, String errorCode) {
        String value = (String) errors.getFieldValue(field);
        if (value != null && value.trim().length() > 0) {
            char[] invalidSymbols = {'<', '\'', '>'};
            for (char invalidSymbol : invalidSymbols) {
                if (value.indexOf(invalidSymbol) >= 0) {
                    errors.rejectValue(field, errorCode);
                }
            }
        }
    }

    /**
     * @param errors
     * @param field
     * @param errorCode
     */
    public static void rejectEmailFormat(Errors errors, String field, String errorCode) {
        String value = (String) errors.getFieldValue(field);
        if (value != null && value.trim().length() > 0) {
            try {
                Matcher matcher = EMAIL_PATTERN.matcher(value);
                if (!matcher.matches()) {
                    errors.rejectValue(field, errorCode);
                }
            } catch (Exception e) {
                errors.rejectValue(field, errorCode);
            }
        }
    }

    /**
     * @param errors
     * @param field
     * @param errorCode
     */
    public static void rejectIPFormat(Errors errors, String field, String errorCode) {
        String value = (String) errors.getFieldValue(field);
        if (value != null && value.trim().length() > 0) {
            Matcher matcher = IP_SKELETON_PATTERN.matcher(value);
            if (!matcher.matches()) {
                errors.rejectValue(field, errorCode);
                return;
            }
            String[] octets = value.split("\\.");
            for (String octet1 : octets) {
                Integer octet = Integer.valueOf(octet1);
                if (octet < 0 || octet > 254) {
                    errors.rejectValue(field, errorCode);
                    return;
                }
            }
        }
    }

    /**
     * @param errors
     * @param field
     * @param errorCode
     */
    public static void rejectSubnetMaskFormat(Errors errors, String field, String errorCode) {
        String value = (String) errors.getFieldValue(field);
        if (value != null && value.trim().length() > 0) {
            Matcher matcher = IP_SKELETON_PATTERN.matcher(value);
            if (!matcher.matches()) {
                errors.rejectValue(field, errorCode);
                return;
            }
            String[] octets = value.split("\\.");
            for (int i = 0; i < octets.length; i++) {
                Integer octet = Integer.valueOf(octets[i]);
                int from = i == 0 ? 1 : 0;
                //int to = 255;
                if (from > octet || octet > 255) {
                    errors.rejectValue(field, errorCode);
                    return;
                }
            }
        }
    }

    /**
     * @param errors
     * @param field
     * @param errorCode
     */
    public static void rejectEmailWithoutAtSign(Errors errors, String field, String errorCode) {
        String value = (String) errors.getFieldValue(field);
        if (value != null && !value.contains("@")) {
            errors.rejectValue(field, errorCode);
        }
    }

    /**
     * @param errors
     * @param field
     * @param errorCode
     */
    public static void rejectNotInteger(Errors errors, String field, String errorCode) {
        String value = (String) errors.getFieldValue(field);
        if (value == null || value.trim().length() == 0) {
            return;
        }

        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            errors.rejectValue(field, errorCode);
        }
    }

    /**
     * @param min
     * @param max
     * @param errors
     * @param field
     * @param errorCode
     */
    public static void rejectIntegerValue(int min, int max, Errors errors, String field, String errorCode) {
        String value = (String) errors.getFieldValue(field);
        if (value == null || value.trim().length() == 0) {
            return;
        }

        try {
            int intValue = Integer.parseInt(value);
            if (intValue < min || intValue > max) {
                errors.rejectValue(field, errorCode, new Object[]{/*Integer.valueOf(min), */max}, "");
            }
        } catch (NumberFormatException e) {
            errors.rejectValue(field, errorCode);
        }
    }

    /**
     * @param errors
     * @param field
     * @param errorCode
     */
    public static void rejectNegativeOrZero(Errors errors, String field, String errorCode) {
        String value = (String) errors.getFieldValue(field);
        if (value == null || value.trim().length() == 0) {
            return;
        }

        try {
            int intValue = Integer.parseInt(value);
            if (intValue <= 0) {
                errors.rejectValue(field, errorCode);
            }
        } catch (NumberFormatException e) {
            errors.rejectValue(field, errorCode);
        }
    }

    /**
     * @param errors
     * @param field
     * @param errorCode
     */
    public static void rejectNotDate(Errors errors, String field, String errorCode) {
        String value = (String) errors.getFieldValue(field);
        if (value == null || value.trim().length() == 0) {
            return;
        }

        try {
            DateFormat df = new SimpleDateFormat();
            df.parse(value);
        } catch (ParseException e) {
            errors.rejectValue(field, errorCode);
        }
    }

    /**
     * @param errors
     * @param field
     * @param errorCode
     * @param before
     */
    public static void rejectNotBefore(Errors errors, String field, String errorCode, Date before) {
        if (errors.getFieldValue(field) != null) {
            Date value = (Date) errors.getFieldValue(field);

            if (value.before(before)) {
                errors.rejectValue(field, errorCode);
            }
        }
    }

    public static void rejectNotTrue(Errors errors, String field, String errorCode) {
        if (errors.getFieldValue(field) != null) {
            boolean value = (boolean) errors.getFieldValue(field);

            if (!value) {
                errors.rejectValue(field, errorCode);
            }
        }
    }

    /**
     * @param errors
     * @param field
     * @param errorCode
     */
    public static void rejectNotLong(Errors errors, String field, String errorCode) {
        String value = (String) errors.getFieldValue(field);
        if (value == null || value.trim().length() == 0) {
            return;
        }

        try {
            Long.parseLong(value);
        } catch (Exception e) {
            errors.rejectValue(field, errorCode);
        }
    }

    /**
     * @param errors
     * @param request
     * @return
     */
    public static Map<String, String> resolveMessages(Errors errors, HttpServletRequest request) {
        Map<String, String> result = new HashMap<>();
        RequestContext requestContext = new RequestContext(request);
        for (FieldError error : errors.getFieldErrors()) {
            String message = requestContext.getMessage(error.getCode(), error.getArguments());
            result.put(error.getField(), message);
        }

        return result;
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
