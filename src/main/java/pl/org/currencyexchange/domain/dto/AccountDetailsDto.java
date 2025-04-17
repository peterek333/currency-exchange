package pl.org.currencyexchange.domain.dto;

import java.math.BigDecimal;

public record AccountDetailsDto(
        Long id,
        String fullName,
        BigDecimal plnBalance,
        BigDecimal eurBalance
) {


}
