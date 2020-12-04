package de.immomio.api.handlers;

import de.immomio.constants.exceptions.ApiValidationException;
import de.immomio.constants.exceptions.BadRequestException;
import de.immomio.constants.exceptions.TokenValidationException;
import de.immomio.web.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class CustomApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " +
                    violation.getPropertyPath() + ": " + violation.getMessage());
        }

        ApiException exception = new ApiException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(exception, new HttpHeaders(), exception.getStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedViolation(AccessDeniedException ex) {
        return errorResponse(ex);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex) {
        return errorResponse(ex);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {
        return errorResponse(ex);
    }

    @ExceptionHandler(ApiValidationException.class)
    public ResponseEntity<Object> handleApiValidationException(ApiValidationException ex) {
        return errorResponse(ex);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        List<String> errors = new ArrayList<>();

        if (ex.getRootCause() instanceof AccessDeniedException) {
            errors.add(ex.getRootCause().getMessage() + ", value=" + ex.getValue());
        } else {
            errors.add(ex.getMessage());
        }

        ApiException exception = new ApiException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(exception, new HttpHeaders(), exception.getStatus());
    }

    @ExceptionHandler({TransactionSystemException.class})
    public ResponseEntity<Object> handleConstraintViolation(Exception ex) {
        Throwable cause = ((TransactionSystemException) ex).getRootCause();
        if (cause instanceof ConstraintViolationException) {
            return handleConstraintViolation((ConstraintViolationException) cause);
        } else {
            List<String> errors = new ArrayList<>();
            errors.add(ex.getMessage());

            ApiException exception = new ApiException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
            return new ResponseEntity<>(exception, new HttpHeaders(), exception.getStatus());
        }
    }

    @ExceptionHandler(TokenValidationException.class)
    public ResponseEntity<Object> handleTokenValidationException(TokenValidationException ex) {
        return errorResponse(ex);
    }

    private ResponseEntity<Object> errorResponse(RuntimeException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getLocalizedMessage());

        ApiException exception = new ApiException(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(exception, new HttpHeaders(), exception.getStatus());
    }

}
