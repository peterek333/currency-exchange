package pl.org.currencyexchange.domain.port.input;

import pl.org.currencyexchange.domain.dto.AccountDetailsDto;
import pl.org.currencyexchange.domain.query.GetAccountQuery;

public interface GetAccountHandler {

    AccountDetailsDto handle(GetAccountQuery getAccountQuery);

}
