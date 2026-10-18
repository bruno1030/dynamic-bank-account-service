package com.dynamicbank.account.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class UpdateBalancesRequestDTO {
    @NotNull
    UUID fromAccountId;

    @NotNull
    UUID toAccountId;

    @NotNull
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero")
    @Digits(integer = 13, fraction = 2)
    BigDecimal amount;
}
