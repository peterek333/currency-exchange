package pl.org.currencyexchange.domain.factory;

import pl.org.currencyexchange.domain.command.ExchangeCurrencyCommand;
import pl.org.currencyexchange.domain.dto.ExchangeCurrencyDto;
import pl.org.currencyexchange.domain.model.Currency;

public class ExchangeCurrencyDtoFactory {

    public static ExchangeCurrencyDto from(ExchangeCurrencyCommand command) {
        return new ExchangeCurrencyDto(
                command.accountId(),
                Currency.from(command.fromCurrency()),
                Currency.from(command.toCurrency()),
                command.amount()
        );
    }

}
