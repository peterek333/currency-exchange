package pl.org.currencyexchange.domain.command;

import java.math.BigDecimal;

public record ExchangeCurrencyCommand(
        Long accountId,
        String fromCurrency,
        String toCurrency,
        BigDecimal amount
) {}
