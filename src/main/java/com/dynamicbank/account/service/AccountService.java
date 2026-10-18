package com.dynamicbank.account.service;

import com.dynamicbank.account.dto.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    public AuthResponseDTO create(@Valid CreateAccountRequestDTO request) {

        return null;
    }

    public AuthResponseDTO authenticate(@Valid LoginRequestDTO request) {
        return null;
    }

    public void closeAccount(UUID uuid) {

    }

    public List<AccountSearchResultDTO> searchAccounts(UUID uuid, @Valid SearchAccountsRequestDTO request) {
        return null;
    }

    public void updateBalances(@Valid UpdateBalancesRequestDTO request) {

    }

    public AccountResponseDTO getAccount(UUID uuid) {
        return null;
    }
}
