package pl.org.currencyexchange.domain.port.output;

import lombok.NonNull;
import pl.org.currencyexchange.domain.dto.AccountDetailsDto;
import pl.org.currencyexchange.domain.dto.CreateAccountDto;
import pl.org.currencyexchange.domain.dto.CreatedAccountDto;

import java.util.Optional;

public interface AccountRepository {

    CreatedAccountDto create(@NonNull CreateAccountDto createAccountDto);
    AccountDetailsDto save(@NonNull AccountDetailsDto accountDetailsDto);
    Optional<AccountDetailsDto> findById(@NonNull Long id);

}
