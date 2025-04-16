package pl.org.currencyexchange.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.org.currencyexchange.api.request.ExchangeCurrencyRequest;

@RestController
@RequestMapping("/api/v1/exchange")
@RequiredArgsConstructor
class CurrencyExchangeController {

    @PostMapping
    void exchangeCurrency(@Valid @RequestBody ExchangeCurrencyRequest exchangeCurrencyRequest) {

    }

}
