package pl.org.currencyexchange.domain.port.output;

import pl.org.currencyexchange.domain.model.Currency;

import java.math.BigDecimal;

public interface PLNExchangeRateHolder {

    BigDecimal getCurrencyExchangeRate(Currency forCurrency);

}
