package pl.org.currencyexchange.domain.port.input;

import pl.org.currencyexchange.domain.command.ExchangeCurrencyCommand;

public interface ExchangeCurrencyHandler {

    void handle(ExchangeCurrencyCommand command);

}
