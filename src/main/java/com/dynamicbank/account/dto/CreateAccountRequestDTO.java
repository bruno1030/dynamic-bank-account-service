package com.dynamicbank.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CreateAccountRequestDTO {

    @NotBlank
    String username;

    @NotBlank
    @Email
    String email;

    @NotBlank
    @Size(min = 8)
    String password;

}
