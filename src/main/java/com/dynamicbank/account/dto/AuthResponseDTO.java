package com.dynamicbank.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponseDTO {
    String token;
    UserResponseDTO user;
}

