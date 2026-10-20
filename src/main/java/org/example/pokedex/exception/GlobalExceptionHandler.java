package org.example.pokedex.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({
            PokemonNotFoundException.class,
            CollectionEntryNotFoundException.class
    })
    public ResponseEntity<ErrorMessage> handleNotFound(
            RuntimeException exception
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> handleDtoValidation(
            MethodArgumentNotValidException exception
    ) {
        String message = exception.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage() == null
                        ? "Invalid input"
                        : error.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining("; "));

        return ResponseEntity.badRequest()
                .body(new ErrorMessage(message));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorMessage> handleParameterValidation(
            HandlerMethodValidationException exception
    ) {
        if (exception.isForReturnValue()) {
            log.error("Response validation failed", exception);

            return ResponseEntity.internalServerError()
                    .body(new ErrorMessage("Internal server error"));
        }

        String message = exception.getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage() == null
                        ? "Invalid parameter"
                        : error.getDefaultMessage())
                .distinct()
                .collect(Collectors.joining("; "));

        return ResponseEntity.badRequest()
                .body(new ErrorMessage(message));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorMessage> handleResponseStatus(
            ResponseStatusException exception
    ) {
        String message = exception.getReason() == null
                ? "Request failed"
                : exception.getReason();

        return ResponseEntity.status(exception.getStatusCode())
                .body(new ErrorMessage(message));
    }
}