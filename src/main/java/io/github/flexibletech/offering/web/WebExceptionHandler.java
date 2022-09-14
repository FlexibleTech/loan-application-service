package io.github.flexibletech.offering.web;

import io.github.flexibletech.offering.application.LoanApplicationNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.MismatchingMessageCorrelationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
class WebExceptionHandler {
    private static final String INTERNAL_ERROR_MESSAGE = "Internal error.";
    private static final String INVALID_LOAN_APPLICATION_STATUS_MESSAGE = "Invalid loan application status for this action.";

    @ExceptionHandler(LoanApplicationNotFoundException.class)
    private void handleLoanApplicationNotFoundException(LoanApplicationNotFoundException ex, HttpServletResponse response) throws IOException {
        log.error(ex.getMessage(), ex);
        response.sendError(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    private void handleDataAccessException(DataAccessException ex, HttpServletResponse response) throws IOException {
        log.error(ex.getMessage(), ex);
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), INTERNAL_ERROR_MESSAGE);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private void handleIllegalArgumentException(IllegalArgumentException ex, HttpServletResponse response) throws IOException {
        log.error(ex.getMessage(), ex);
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    private void handleIllegalStateException(IllegalStateException ex, HttpServletResponse response) throws IOException {
        log.error(ex.getMessage(), ex);
        response.sendError(HttpStatus.CONFLICT.value(), ex.getMessage());
    }

    @ExceptionHandler(MismatchingMessageCorrelationException.class)
    private void handleMismatchingMessageCorrelationException(MismatchingMessageCorrelationException ex, HttpServletResponse response) throws IOException {
        log.error(ex.getMessage(), ex);
        response.sendError(HttpStatus.CONFLICT.value(), INVALID_LOAN_APPLICATION_STATUS_MESSAGE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private void handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletResponse response) throws IOException {
        log.error(ex.getMessage(), ex);
        var error = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(this::errorToString)
                .collect(Collectors.joining(";"));

        response.sendError(HttpStatus.BAD_REQUEST.value(), error);
    }

    private String errorToString(ObjectError objectError) {
        if (objectError instanceof FieldError) {
            FieldError fieldError = (FieldError) objectError;
            return String.format("field error: %s - %s, actual value: [%s]",
                    fieldError.getField(), fieldError.getDefaultMessage(), fieldError.getRejectedValue());
        }
        return String.format("object error: %s - %s", objectError.getObjectName(), objectError.getDefaultMessage());
    }

}
