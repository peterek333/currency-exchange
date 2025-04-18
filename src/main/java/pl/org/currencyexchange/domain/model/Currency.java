package pl.org.currencyexchange.domain.model;

import pl.org.currencyexchange.domain.exception.NotSupportedCurrencyException;

public enum Currency {
    EUR,
    PLN
    ;

    public static Currency from(String currency) {
        try {
            return Currency.valueOf(currency);
        } catch (IllegalArgumentException e) {
            throw new NotSupportedCurrencyException(currency);
        }
    }
}
