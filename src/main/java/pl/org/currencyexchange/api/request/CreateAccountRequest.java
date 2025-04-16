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

        @NotNull(message = "Initial balance is required")
        @DecimalMin(value = "0.0", message = "Initial balance must be 0 or greater")
        BigDecimal initialBalance
) {}
