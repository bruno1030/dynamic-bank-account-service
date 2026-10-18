package com.dynamicbank.account.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountResponseDTO {
    UUID accountId;
    String accountNumber;
    BigDecimal balance;
    String currency;
    String status;
}
