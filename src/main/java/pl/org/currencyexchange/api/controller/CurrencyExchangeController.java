package pl.org.currencyexchange.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.org.currencyexchange.api.request.ExchangeCurrencyRequest;
import pl.org.currencyexchange.domain.command.ExchangeCurrencyCommand;
import pl.org.currencyexchange.domain.port.input.ExchangeCurrencyHandler;

@Slf4j
@RestController
@RequestMapping("/api/v1/exchange")
@RequiredArgsConstructor
class CurrencyExchangeController {

    private final ExchangeCurrencyHandler exchangeCurrencyHandler;

    @PostMapping
    void exchangeCurrency(@Valid @RequestBody ExchangeCurrencyRequest exchangeCurrencyRequest) {
        log.info("Exchange currency request: {}", exchangeCurrencyRequest);

        var command = new ExchangeCurrencyCommand(
                exchangeCurrencyRequest.accountId(),
                exchangeCurrencyRequest.fromCurrency(),
                exchangeCurrencyRequest.toCurrency(),
                exchangeCurrencyRequest.amount()
        );

        exchangeCurrencyHandler.handle(command);
    }

}
