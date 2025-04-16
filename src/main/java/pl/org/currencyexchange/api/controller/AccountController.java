package pl.org.currencyexchange.api.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.org.currencyexchange.api.request.CreateAccountRequest;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
// fixme - dodać w nazwie V1 -> AccountControllerV1, analogicznie dla pozostałych wersjonowanych API
class AccountController {

    @PostMapping
    void createAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest) {

    }

    @GetMapping("/{accountId}")
//    todo - wymagane @Valid? albo @Validated, bo String nie wspiera @Valid?
    void getAccount(@NotBlank(message = "Account ID is required") @PathVariable String accountId) {

    }

}
