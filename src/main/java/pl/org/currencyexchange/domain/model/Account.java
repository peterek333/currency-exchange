package pl.org.currencyexchange.domain.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Account {

    Long id;
    String fullName;
    BigDecimal plnBalance;
    BigDecimal eurBalance;

}
