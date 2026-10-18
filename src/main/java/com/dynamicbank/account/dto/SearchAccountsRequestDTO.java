package com.dynamicbank.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Size;

public class SearchAccountsRequestDTO {
    @Size(min = 3, message = "O username deve ter no mínimo 3 caracteres")
    String username;

    @Size(min = 3, message = "O email deve ter no mínimo 3 caracteres")
    String email;

    @JsonIgnore
    @AssertTrue(message = "Informe username ou email")
    public boolean isAtLeastOneFilled() {
        return hasText(username) || hasText(email);
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
