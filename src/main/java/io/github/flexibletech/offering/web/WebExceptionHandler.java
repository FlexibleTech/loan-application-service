package io.github.flexibletech.offering.web;

import io.github.flexibletech.offering.application.LoanApplicationNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice(annotations = RestController.class)
public class WebExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(LoanApplicationNotFoundException.class)
    private ResponseEntity<WebError> catchLoanApplicationNotFoundException(LoanApplicationNotFoundException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new WebError(
                        HttpStatus.NOT_FOUND,
                        LocalDateTime.now(),
                        ex.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    private ResponseEntity<WebError> catchDataAccessException(DataAccessException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new WebError(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        LocalDateTime.now(),
                        ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<WebError> catchIllegalArgumentException(IllegalArgumentException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new WebError(
                        HttpStatus.BAD_REQUEST,
                        LocalDateTime.now(),
                        ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    private ResponseEntity<WebError> catchIllegalStateException(IllegalStateException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new WebError(
                        HttpStatus.CONFLICT,
                        LocalDateTime.now(),
                        ex.getMessage()));
    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NotNull MethodArgumentNotValidException ex,
                                                                  @NotNull HttpHeaders headers,
                                                                  @NotNull HttpStatus status,
                                                                  @NotNull WebRequest request) {
        log.error(ex.getMessage(), ex);
        var error = ex.getBindingResult().getAllErrors()
                .stream()
                .map(this::errorToString)
                .collect(Collectors.joining(";"));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new WebError(
                        HttpStatus.BAD_REQUEST,
                        LocalDateTime.now(),
                        error));
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
