package pl.org.currencyexchange.nbp_adapter.exception;

public class ExchangeRateProcessingException extends RuntimeException {
    public ExchangeRateProcessingException(Exception e) {
        super("Exchange rate processing error", e);
    }
}
