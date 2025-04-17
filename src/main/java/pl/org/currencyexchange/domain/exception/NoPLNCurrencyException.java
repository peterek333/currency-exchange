package pl.org.currencyexchange.domain.exception;

public class NoPLNCurrencyException extends BusinessException {
    public NoPLNCurrencyException() {
        super("Same currencies were sent. Please change the type of one of them.");
    }
}
