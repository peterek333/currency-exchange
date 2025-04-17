package pl.org.currencyexchange.domain.factory;

import pl.org.currencyexchange.domain.command.ExchangeCurrencyCommand;
import pl.org.currencyexchange.domain.dto.ExchangeFactoryDto;
import pl.org.currencyexchange.domain.model.Currency;

public class ExchangeCurrencyDtoFactory {

    public static ExchangeFactoryDto from(ExchangeCurrencyCommand command) {
        return new ExchangeFactoryDto(
                command.accountId(),
                Currency.from(command.fromCurrency()),
                Currency.from(command.toCurrency()),
                command.amount()
        );
    }

}
