package pl.org.currencyexchange.domain.dto;

import pl.org.currencyexchange.domain.model.Currency;

import java.math.BigDecimal;

public record ExchangeCurrencyDto(
        Long accountId,
        Currency from,
        Currency to,
        BigDecimal amount
) {}
