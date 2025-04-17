package pl.org.currencyexchange.nbp_adapter.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.org.currencyexchange.domain.model.Currency;

@Component
public class NbpWebClient {

    private final RestTemplate restTemplate = new RestTemplate();

//    fixme - think about using Table C URL with purchase/sale price (with spread?)
    private static final String NBP_EXCHANGE_RATES_A_URL = "https://api.nbp.pl/api/exchangerates/rates/a/";

    public String fetchExchangeRate(Currency forCurrency) {
        var response = restTemplate.getForObject(
                NBP_EXCHANGE_RATES_A_URL + forCurrency.name().toLowerCase(),
                String.class
        );

        return response;
    }

}
