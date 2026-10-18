package com.dynamicbank.account.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDTO {

    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;

}