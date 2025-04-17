package pl.org.currencyexchange.persistence_adapter.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "pl.org.currencyexchange.persistence_adapter.repository")
@EntityScan(basePackages = "pl.org.currencyexchange.persistence_adapter.entity")
class EnableJpaRepositoriesConfiguration {}
