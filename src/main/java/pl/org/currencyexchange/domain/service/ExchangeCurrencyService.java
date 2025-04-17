package pl.org.currencyexchange.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.org.currencyexchange.domain.command.ExchangeCurrencyCommand;
import pl.org.currencyexchange.domain.dto.AccountDetailsDto;
import pl.org.currencyexchange.domain.dto.ExchangeAmountCalculated;
import pl.org.currencyexchange.domain.dto.ExchangeCurrencyDto;
import pl.org.currencyexchange.domain.exception.AccountNotFoundException;
import pl.org.currencyexchange.domain.exception.BusinessException;
import pl.org.currencyexchange.domain.exception.NoPLNCurrencyException;
import pl.org.currencyexchange.domain.exception.SameCurrencyException;
import pl.org.currencyexchange.domain.factory.ExchangeCurrencyDtoFactory;
import pl.org.currencyexchange.domain.model.Currency;
import pl.org.currencyexchange.domain.port.input.ExchangeCurrencyHandler;
import pl.org.currencyexchange.domain.port.output.AccountRepository;
import pl.org.currencyexchange.domain.port.output.PLNExchangeRateHolder;
import pl.org.currencyexchange.domain.properties.CurrencyExchangeProperties;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Slf4j
@Service
@RequiredArgsConstructor
class ExchangeCurrencyService implements ExchangeCurrencyHandler {

    private final AccountRepository accountRepository;
    private final PLNExchangeRateHolder plnExchangeRateHolder;
    private final CurrencyExchangeProperties currencyExchangeProperties;

    @Override
    @Transactional
    public void handle(ExchangeCurrencyCommand command) {
        var exchangeCurrencyDto = ExchangeCurrencyDtoFactory.from(command);

        validateCurrencies(exchangeCurrencyDto.from(), exchangeCurrencyDto.to());

        var account = accountRepository.findById(exchangeCurrencyDto.accountId())
                .orElseThrow(() -> new AccountNotFoundException(exchangeCurrencyDto.accountId()));

        Currency forCurrency = getNotPLNCurrency(exchangeCurrencyDto.from(), exchangeCurrencyDto.to());
        var currencyExchangeRate = plnExchangeRateHolder.getCurrencyExchangeRate(forCurrency);
        log.debug("Currency exchange rate: {}", currencyExchangeRate);

        var exchangeAmountCalculated = calculateCurrencyAmount(exchangeCurrencyDto, currencyExchangeRate);
        log.debug("Calculated exchange amount: {}", exchangeAmountCalculated);

        validateRequiredAmount(exchangeCurrencyDto.from(), account, exchangeAmountCalculated);

        var changedAccount = exchangeCurrencies(account, exchangeAmountCalculated);
//        fixme - shouldn't log "Account" data
        log.debug("Account before change: {}", account);
        log.debug("Account after change: {}", changedAccount);

        accountRepository.save(changedAccount);
    }

    //    fixme - refactor to dedicated class SRP
    private void validateRequiredAmount(Currency from, AccountDetailsDto account, ExchangeAmountCalculated exchangeAmountCalculated) {
        if (from == Currency.PLN) {
            if (account.plnBalance().add(exchangeAmountCalculated.plnAmountToChange()).compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException(String.format("No required amount. Current PLN balance: %s but requires %s PLN",
                        account.plnBalance(), exchangeAmountCalculated.plnAmountToChange().negate()));
            }
        } else {
            if (account.eurBalance().add(exchangeAmountCalculated.eurAmountToChange()).compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException(String.format("No required amount. Current EUR balance: %s but requires %s EUR",
                        account.eurBalance(), exchangeAmountCalculated.eurAmountToChange().negate()));
            }
        }
    }

    //    fixme - refactor to dedicated class SRP
    private ExchangeAmountCalculated calculateCurrencyAmount(ExchangeCurrencyDto exchangeCurrencyDto, BigDecimal currencyExchangeRate) {
        BigDecimal plnAmountToChange;
        BigDecimal eurAmountToChange;

        if (exchangeCurrencyDto.from() == Currency.PLN) {
            eurAmountToChange = exchangeCurrencyDto.amount()
                    .divide(currencyExchangeRate, currencyExchangeProperties.getRoundingScale(), RoundingMode.HALF_EVEN);

            plnAmountToChange = exchangeCurrencyDto.amount().negate();
        } else {
            plnAmountToChange = exchangeCurrencyDto.amount()
                    .multiply(currencyExchangeRate)
                    .setScale(currencyExchangeProperties.getRoundingScale(), RoundingMode.HALF_EVEN);;

            eurAmountToChange = exchangeCurrencyDto.amount().negate();
        }

        return new ExchangeAmountCalculated(plnAmountToChange, eurAmountToChange);
    }

    private AccountDetailsDto exchangeCurrencies(AccountDetailsDto account, ExchangeAmountCalculated exchangeAmountCalculated) {
        return new AccountDetailsDto(
                account.id(),
                account.fullName(),
                account.plnBalance().add(exchangeAmountCalculated.plnAmountToChange()),
                account.eurBalance().add(exchangeAmountCalculated.eurAmountToChange())
        );
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
