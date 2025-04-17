package pl.org.currencyexchange.domain.dto;

import java.math.BigDecimal;

public record CreateAccountDto(
        String fullName,
        BigDecimal plnInitialBalance,
        BigDecimal eurInitialBalance
) {}
