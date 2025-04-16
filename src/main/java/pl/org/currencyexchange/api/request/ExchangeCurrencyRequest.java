package pl.org.currencyexchange.api.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ExchangeCurrencyRequest(
        @NotNull(message = "From currency is required")
        String fromCurrency,

        @NotNull(message = "To currency is required")
        String toCurrency,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.00", inclusive = false, message = "Amount must be greater than 0")
        BigDecimal amount
) {}
