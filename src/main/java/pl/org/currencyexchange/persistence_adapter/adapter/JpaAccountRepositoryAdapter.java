package pl.org.currencyexchange.persistence_adapter.adapter;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.org.currencyexchange.domain.dto.AccountDetailsDto;
import pl.org.currencyexchange.domain.dto.CreateAccountDto;
import pl.org.currencyexchange.domain.dto.CreatedAccountDto;
import pl.org.currencyexchange.domain.port.output.AccountRepository;
import pl.org.currencyexchange.persistence_adapter.entity.AccountEntity;
import pl.org.currencyexchange.persistence_adapter.repository.AccountJpaRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class JpaAccountRepositoryAdapter implements AccountRepository {

    private final AccountJpaRepository accountJpaRepository;

    @Override
    public CreatedAccountDto create(@NonNull CreateAccountDto createAccountDto) {
        var accountEntity = prepareEntity(createAccountDto);

        var createdAccount = accountJpaRepository.save(accountEntity);

        return new CreatedAccountDto(createdAccount.getId());
    }

    @Override
    public AccountDetailsDto save(@NonNull AccountDetailsDto accountDetailsDto) {
        var accountEntity = prepareEntity(accountDetailsDto);

        var changedAccountEntity = accountJpaRepository.save(accountEntity);

        return new AccountDetailsDto(
                changedAccountEntity.getId(),
                changedAccountEntity.getFullName(),
                changedAccountEntity.getPlnBalance(),
                changedAccountEntity.getEurBalance()
        );
    }

    @Override
    public Optional<AccountDetailsDto> findById(@NonNull Long id) {
        return accountJpaRepository.findById(id)
                .map(accountEntity -> new AccountDetailsDto(
                        accountEntity.getId(),
                        accountEntity.getFullName(),
                        accountEntity.getPlnBalance(),
                        accountEntity.getEurBalance()
                ));
    }

//    fixme - refactor to dedicated class
    private AccountEntity prepareEntity(CreateAccountDto createAccountDto) {
        var accountEntity = new AccountEntity();
        accountEntity.setFullName(createAccountDto.fullName());
        accountEntity.setPlnBalance(createAccountDto.plnInitialBalance());
        accountEntity.setEurBalance(createAccountDto.eurInitialBalance());
        return accountEntity;
    }

    //    fixme - refactor to dedicated class
    private AccountEntity prepareEntity(@NonNull AccountDetailsDto accountDetailsDto) {
        var accountEntity = new AccountEntity();
        accountEntity.setId(accountDetailsDto.id());
        accountEntity.setFullName(accountDetailsDto.fullName());
        accountEntity.setPlnBalance(accountDetailsDto.plnBalance());
        accountEntity.setEurBalance(accountDetailsDto.eurBalance());
        return accountEntity;
    }
}
