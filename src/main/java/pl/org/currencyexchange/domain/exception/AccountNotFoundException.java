package pl.org.currencyexchange.domain.exception;

public class AccountNotFoundException extends BusinessException {
    public AccountNotFoundException(long accountId) {
        super(String.format("Account with ID=%s was not found.", accountId));
    }
}
