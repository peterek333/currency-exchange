package pl.org.currencyexchange.domain.command;

import java.math.BigDecimal;

public record CreateAccountCommand(
        String firstName,
        String lastName,
        BigDecimal plnInitialBalance
) {}
