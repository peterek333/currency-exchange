package pl.org.currencyexchange.domain.port.input;

import pl.org.currencyexchange.domain.command.CreateAccountCommand;
import pl.org.currencyexchange.domain.dto.CreatedAccountDto;

public interface CreateAccountHandler {

    CreatedAccountDto handle(CreateAccountCommand command);

}
