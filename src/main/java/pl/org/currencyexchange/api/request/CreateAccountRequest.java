package pl.org.currencyexchange.api.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateAccountRequest(
        @NotBlank(message = "First name is required")
        String firstName,

        @NotBlank(message = "Last name is required")
        String lastName,

//      fixme - better validation on business level (dynamic property of scale/decimal places and not required "integer" part)
//        @Digits(integer = 10, fraction = 4, message = "Initial balance must have no more than 4 decimal places")
        @NotNull(message = "Initial balance is required")
        @DecimalMin(value = "0.0", message = "Initial balance must be 0 or greater")
        BigDecimal plnInitialBalance
) {}
