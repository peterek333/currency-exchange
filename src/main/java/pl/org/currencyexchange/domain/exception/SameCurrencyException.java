package pl.org.currencyexchange.domain.exception;

public class SameCurrencyException extends BusinessException {
    public SameCurrencyException() {
        super("Same currencies were sent. Please change the type of one of them.");
    }
}
