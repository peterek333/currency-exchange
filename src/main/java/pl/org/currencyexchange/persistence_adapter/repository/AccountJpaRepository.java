package pl.org.currencyexchange.persistence_adapter.repository;

import org.springframework.stereotype.Repository;
import pl.org.currencyexchange.persistence_adapter.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AccountJpaRepository extends JpaRepository<AccountEntity, Long> {
}
