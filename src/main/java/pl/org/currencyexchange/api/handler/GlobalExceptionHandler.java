package pl.org.currencyexchange.api.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.org.currencyexchange.domain.exception.BusinessException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handle(MethodArgumentNotValidException exception) {
        var fieldErrors = exception.getBindingResult().getFieldErrors();
        String errorMessage =  fieldErrors.isEmpty()
                ? "Validation error."
                : fieldErrors.stream()
                    .map(error -> String.format("Field '%s': %s", error.getField(), error.getDefaultMessage()))
                    .collect(Collectors.joining(", ", "Validation errors: ", ""));

        log.error("Validation error occured: {}", errorMessage);
        return new ErrorResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(BusinessException.class)
    public ErrorResponse handle(BusinessException exception) {
        log.error("Business error occured: {}", exception.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

}
