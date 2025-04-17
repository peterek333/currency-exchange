package pl.org.currencyexchange.domain.dto;

import java.math.BigDecimal;

public record ExchangeAmountCalculated(
        BigDecimal plnAmountToChange,
        BigDecimal eurAmountToChange
) {}
