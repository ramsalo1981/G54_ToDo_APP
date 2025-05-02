package se.lexicon.todo_app.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class MyExceptionHandler {
    // Handle resource not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return createErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Handle data duplicates
    @ExceptionHandler(DataDuplicateException.class)
    public ResponseEntity<ErrorResponse> handleDataDuplicate(DataDuplicateException ex) {
        return createErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // Handle resource not found
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex) {
        String errorMessage = "Resource not found: " + ex.getMessage();
        return createErrorResponse(HttpStatus.NOT_FOUND, errorMessage);
    }

    // Handle invalid URI parameters (type mismatch)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String invalidParamMessage = "Parameter '%s' should be of type %s";
        String errorMessage = String.format(invalidParamMessage,
                ex.getName(),
                ex.getRequiredType().getSimpleName());
        return createErrorResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }

    // Handle validation errors
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleValidationViolation(ConstraintViolationException ex) {
        String[] violations = ex.getConstraintViolations().stream()
                .map(violation -> violation.getMessage())
                .toArray(String[]::new);
        return createErrorResponse(HttpStatus.BAD_REQUEST, violations);
    }

    // Handle runtime exceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        String uuid = java.util.UUID.randomUUID().toString().toUpperCase();
        System.err.println("Error ID: " + uuid + " - " + ex.getMessage());
        return createErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred. Reference ID: " + uuid);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(HttpStatus status, String... errors) {
        ErrorResponse errorResponse = new ErrorResponse(status.value(), errors);
        return new ResponseEntity<>(errorResponse, status);
    }


    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
