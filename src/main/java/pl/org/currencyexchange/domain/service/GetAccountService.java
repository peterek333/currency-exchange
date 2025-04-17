package pl.org.currencyexchange.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.org.currencyexchange.domain.dto.AccountDetailsDto;
import pl.org.currencyexchange.domain.exception.AccountNotFoundException;
import pl.org.currencyexchange.domain.port.input.GetAccountHandler;
import pl.org.currencyexchange.domain.port.output.AccountRepository;
import pl.org.currencyexchange.domain.query.GetAccountQuery;

@Service
@RequiredArgsConstructor
class GetAccountService implements GetAccountHandler {

    private final AccountRepository accountRepository;

    @Override
    @Transactional(readOnly = true)
    public AccountDetailsDto handle(GetAccountQuery getAccountQuery) {
        return accountRepository.findById(getAccountQuery.accountId())
                .orElseThrow(() -> new AccountNotFoundException(getAccountQuery.accountId()));
    }
}
