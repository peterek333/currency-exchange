package pl.org.currencyexchange.api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.org.currencyexchange.api.request.CreateAccountRequest;
import pl.org.currencyexchange.api.response.AccountDetailsResponse;
import pl.org.currencyexchange.api.response.CreatedAccountResponse;
import pl.org.currencyexchange.domain.command.CreateAccountCommand;
import pl.org.currencyexchange.domain.port.input.CreateAccountHandler;
import pl.org.currencyexchange.domain.port.input.GetAccountHandler;
import pl.org.currencyexchange.domain.query.GetAccountQuery;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
// fixme - add to name V1 -> AccountControllerV1, same for another Controllers
class AccountController {

    private final CreateAccountHandler createAccountHandler;
    private final GetAccountHandler getAccountHandler;

    @PostMapping
    CreatedAccountResponse createAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest) {
        var command = new CreateAccountCommand(
                createAccountRequest.firstName(),
                createAccountRequest.lastName(),
                createAccountRequest.plnInitialBalance()
        );
        var createdAccount = createAccountHandler.handle(command);

        return new CreatedAccountResponse(createdAccount.id());
    }

    @GetMapping("/{accountId}")
    AccountDetailsResponse getAccount(@PathVariable @NotNull(message = "Account ID is required") Long accountId) {
        var query = new GetAccountQuery(accountId);

        var accountDetails = getAccountHandler.handle(query);

        return new AccountDetailsResponse(
                accountDetails.id(),
                accountDetails.fullName(),
                accountDetails.plnBalance(),
                accountDetails.eurBalance()
        );
    }

}
