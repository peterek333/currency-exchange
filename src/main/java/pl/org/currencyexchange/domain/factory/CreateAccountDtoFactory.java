package pl.org.currencyexchange.domain.factory;

import pl.org.currencyexchange.domain.command.CreateAccountCommand;
import pl.org.currencyexchange.domain.dto.CreateAccountDto;

import java.math.BigDecimal;

public class CreateAccountDtoFactory {

    public static CreateAccountDto from(CreateAccountCommand command) {
        return new CreateAccountDto(
                command.firstName() + " " + command.lastName(),
                command.plnInitialBalance(),
                BigDecimal.ZERO
        );
    }
}
