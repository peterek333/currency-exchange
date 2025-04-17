package pl.org.currencyexchange.api.response;

import java.math.BigDecimal;

public record AccountDetailsResponse(
        Long id,
        String fullName,
        BigDecimal plnInitialBalance,
        BigDecimal eurInitialBalance
) {}
