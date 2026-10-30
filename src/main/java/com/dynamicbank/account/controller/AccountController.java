package com.dynamicbank.account.controller;

import com.dynamicbank.account.dto.*;
import com.dynamicbank.account.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // ---- only available for webapp ----
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDTO createAccount(@Valid @RequestBody CreateAccountRequestDTO request) {
        //return accountService.create(request);
        return null;
    }

    // ---- only available for webapp ----
    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        return accountService.authenticate(request);
    }

    // ---- Protegidos por JWT (clientId vem sempre do token) ----

    @GetMapping("/get-account")
    public AccountResponseDTO getAccount(@AuthenticationPrincipal Jwt jwt) {
        return accountService.getAccount(clientIdFrom(jwt));
    }

    @PostMapping("/delete")
    public void deleteAccount(@AuthenticationPrincipal Jwt jwt) {
        accountService.closeAccount(clientIdFrom(jwt));
    }

    @PostMapping("/get-accounts")
    public List<AccountSearchResultDTO> getAccounts(@AuthenticationPrincipal Jwt jwt,
                                                    @Valid @RequestBody SearchAccountsRequestDTO request) {
        return accountService.searchAccounts(clientIdFrom(jwt), request);
    }

    // ---- Protegido por API key (validada no filtro de segurança, não aqui) ----

    // ---- only available for TransferService microservice ----
    @PostMapping("/update-saldos")
    public void updateSaldos(@Valid @RequestBody UpdateBalancesRequestDTO request) {
        accountService.updateBalances(request);
    }

    private UUID clientIdFrom(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }
}