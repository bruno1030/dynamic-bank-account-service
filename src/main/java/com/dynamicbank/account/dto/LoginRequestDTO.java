package com.dynamicbank.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDTO {

    @NotBlank
    String usernameOrEmail;

    @NotBlank
    String password;

}
