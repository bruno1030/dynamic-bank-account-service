package com.dynamicbank.account.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CreateAccountRequestDTO {

    @NotBlank
    @Size(max = 150)
    private String fullName;

    @NotBlank
    @Size(min = 3, max = 50)
    @Pattern(regexp = "^[A-Za-z0-9._-]+$",
            message = "O username só pode conter letras, números, ponto, hífen e underscore")
    private String username;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @NotNull
    @Past
    private LocalDate birthDate;

    @NotBlank
    @Size(min = 8, max = 72)
    private String password;

}