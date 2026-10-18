package com.dynamicbank.account.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class BalanceResponseDTO {
    UUID accountId;
    BigDecimal balance;
    String currency;
}
