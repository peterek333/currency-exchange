package pl.org.currencyexchange.domain.exception;

import pl.org.currencyexchange.domain.model.Currency;

import java.util.Arrays;

public class NotSupportedCurrencyException extends BusinessException {
    public NotSupportedCurrencyException(String currency) {
        super(String.format("Currency '%s' is not supported. Please use one of: %s", currency, Arrays.toString(Currency.values())));
    }
}
