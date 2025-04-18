package pl.org.currencyexchange.domain.service

import pl.org.currencyexchange.domain.command.ExchangeCurrencyCommand
import pl.org.currencyexchange.domain.dto.AccountDetailsDto
import pl.org.currencyexchange.domain.model.Currency
import pl.org.currencyexchange.domain.port.output.AccountRepository
import pl.org.currencyexchange.domain.port.output.PLNExchangeRateHolder
import pl.org.currencyexchange.domain.properties.CurrencyExchangeProperties
import spock.lang.Specification
import spock.lang.Unroll

class ExchangeCurrencyServiceSpec extends Specification {

//    fixme - more test cases
//     PLN -> EUR
//     EUR -> PLN
//     results like 26.3434787 => 26.3435
//     exceptions - input data, amount etc.

    AccountRepository accountRepository = Mock()
    PLNExchangeRateHolder plnExchangeRateHolder = Mock()
    CurrencyExchangeProperties currencyExchangeProperties = Mock()
    ExchangeCurrencyService exchangeCurrencyService = new ExchangeCurrencyService(
            accountRepository, plnExchangeRateHolder, currencyExchangeProperties
    )

    @Unroll
    def "should correctly exchange PLN to EUR with exchangeRate=#exchangeRate and initial PLN balance=#initialPlnBalance"() {
        given:
        def accountId = 1L
        def command = new ExchangeCurrencyCommand(
                accountId, "PLN", "EUR", amountToExchange
        )

        def accountBefore = new AccountDetailsDto(
                accountId,
                "Test User",
                initialPlnBalance,
                initialEurBalance
        )

        plnExchangeRateHolder.getCurrencyExchangeRate(_ as Currency) >> exchangeRate
        currencyExchangeProperties.getRoundingScale() >> 2
        accountRepository.findById(accountId) >> Optional.of(accountBefore)

        when:
        exchangeCurrencyService.handle(command)

        then:
        1 * accountRepository.save({
            it.plnBalance() == expectedPlnBalance &&
                    it.eurBalance() == expectedEurBalance
        })

        where:
        exchangeRate  | initialPlnBalance  | initialEurBalance | amountToExchange || expectedPlnBalance | expectedEurBalance
        4.00G         | 1000.00G           | 100.00G           | 400.00G          || 600.00G            | 200.00G
        4.50G         | 500.00G            | 50.00G            | 450.00G          || 50.00G             | 150.00G
        5.00G         | 200.00G            | 20.00G            | 100.00G          || 100.00G            | 40.00G
    }
}