package com.dynamicbank.account.dto;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
public class AccountResponseDTO {
    UUID accountId;
    String accountNumber;
    BigDecimal balance;
    String currency;
    String status;
}
