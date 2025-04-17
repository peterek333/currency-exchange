package pl.org.currencyexchange.nbp_adapter.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.org.currencyexchange.domain.model.Currency;
import pl.org.currencyexchange.domain.port.output.PLNExchangeRateHolder;
import pl.org.currencyexchange.nbp_adapter.client.NbpWebClient;
import pl.org.currencyexchange.nbp_adapter.exception.ExchangeRateProcessingException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
class PLNExchangeRateHolderFromNbp implements PLNExchangeRateHolder {

    private final NbpWebClient nbpWebClient;

//    fixme - refactor to cache according to NBP data refresh schedule (1 per day?)
    @Override
    public BigDecimal getCurrencyExchangeRate(Currency forCurrency) {
        String responseBody = nbpWebClient.fetchExchangeRate(forCurrency);

        try {
            var root = new ObjectMapper().readTree(responseBody);
            return root.path("rates").get(0).path("mid").decimalValue();
        } catch (JsonProcessingException e) {
            throw new ExchangeRateProcessingException(e);
        }
    }

}
