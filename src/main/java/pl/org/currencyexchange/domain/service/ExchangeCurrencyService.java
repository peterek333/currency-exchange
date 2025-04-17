package pl.org.currencyexchange.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.org.currencyexchange.domain.command.ExchangeCurrencyCommand;
import pl.org.currencyexchange.domain.dto.AccountDetailsDto;
import pl.org.currencyexchange.domain.exception.AccountNotFoundException;
import pl.org.currencyexchange.domain.exception.BusinessException;
import pl.org.currencyexchange.domain.exception.NoPLNCurrencyException;
import pl.org.currencyexchange.domain.exception.SameCurrencyException;
import pl.org.currencyexchange.domain.factory.ExchangeCurrencyDtoFactory;
import pl.org.currencyexchange.domain.model.Currency;
import pl.org.currencyexchange.domain.port.input.ExchangeCurrencyHandler;
import pl.org.currencyexchange.domain.port.output.AccountRepository;
import pl.org.currencyexchange.domain.port.output.PLNExchangeRateHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
class ExchangeCurrencyService implements ExchangeCurrencyHandler {

//    fixme - align with other places with BigDecimal
    private static final int ACCURACY_PLACES = 4;

    private final AccountRepository accountRepository;
    private final PLNExchangeRateHolder plnExchangeRateHolder;

    @Override
    @Transactional
    public void handle(ExchangeCurrencyCommand command) {
        //todo synchronize process
        // option 1 - lock - doesnt support 1+ instances
        // option 2 - select for update?

        //todo - refactor
        //  SRP
        //  rethink to move NBP call outside transaction

        var exchangeCurrencyDto = ExchangeCurrencyDtoFactory.from(command);

        validateCurrencies(exchangeCurrencyDto.from(), exchangeCurrencyDto.to());

        var account = accountRepository.findById(exchangeCurrencyDto.accountId())
                .orElseThrow(() -> new AccountNotFoundException(exchangeCurrencyDto.accountId()));

        Currency forCurrency = getNotPLNCurrency(exchangeCurrencyDto.from(), exchangeCurrencyDto.to());
        var currencyExchangeRate = plnExchangeRateHolder.getCurrencyExchangeRate(forCurrency);

        BigDecimal plnAmountToChange;
        BigDecimal eurAmountToChange;
        if (exchangeCurrencyDto.from() == Currency.PLN) {
            eurAmountToChange = exchangeCurrencyDto.amount()
                    //fixme - ACCURACE_PLACES requires align with another places because currently it may give inaccurate conversion
                    .divide(currencyExchangeRate, ACCURACY_PLACES, RoundingMode.HALF_UP);

            plnAmountToChange = exchangeCurrencyDto.amount().negate();

            //todo refaktor na czytelniejsza metode
            if (account.plnBalance().add(plnAmountToChange).compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException(String.format("No required amount. Current PLN balance: %s but requires %s PLN",
                        account.plnBalance(), plnAmountToChange.negate()));
            }
        } else {
            plnAmountToChange = exchangeCurrencyDto.amount().multiply(currencyExchangeRate);

            eurAmountToChange = exchangeCurrencyDto.amount().negate();

            //todo refaktor na czytelniejsza metode
            if (account.eurBalance().add(eurAmountToChange).compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException(String.format("No required amount. Current EUR balance: %s but requires %s EUR",
                        account.eurBalance(), eurAmountToChange.negate()));
            }
        }

        var changedAccount = new AccountDetailsDto(
                account.id(),
                account.fullName(),
                account.plnBalance().add(plnAmountToChange),
                account.eurBalance().add(eurAmountToChange)
        );

        accountRepository.save(changedAccount);
    }

//    fixme - refactor to dedicated class SRP
    private void validateCurrencies(Currency from, Currency to) {
        if (from == to) {
            throw new SameCurrencyException();
        }
        if (noPLNCurrency(from, to)) {
            throw new NoPLNCurrencyException();
        }
    }

    private boolean noPLNCurrency(Currency from, Currency to) {
        return from != Currency.PLN && to != Currency.PLN;
    }

    //    fixme - refactor to dedicated class
    private Currency getNotPLNCurrency(Currency from, Currency to) {
        return from != Currency.PLN ? from : to;
    }

}
