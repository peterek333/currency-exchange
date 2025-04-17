package pl.org.currencyexchange.api.handler;

import lombok.Value;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Value
class ErrorResponse {
    int status;
    String errorMessage;
    ZonedDateTime timestamp = ZonedDateTime.now();

    ErrorResponse(HttpStatus status, String errorMessage) {
        this.status = status.value();
        this.errorMessage = errorMessage;
    }
}