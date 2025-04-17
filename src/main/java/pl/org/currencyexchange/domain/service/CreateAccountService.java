package pl.org.currencyexchange.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.org.currencyexchange.domain.factory.CreateAccountDtoFactory;
import pl.org.currencyexchange.domain.command.CreateAccountCommand;
import pl.org.currencyexchange.domain.dto.CreatedAccountDto;
import pl.org.currencyexchange.domain.port.input.CreateAccountHandler;
import pl.org.currencyexchange.domain.port.output.AccountRepository;

@Service
@RequiredArgsConstructor
class CreateAccountService implements CreateAccountHandler {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public CreatedAccountDto handle(CreateAccountCommand command) {
        var createAccountDto = CreateAccountDtoFactory.from(command);

        return accountRepository.create(createAccountDto);
    }

}
