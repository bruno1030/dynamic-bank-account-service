package com.dynamicbank.account.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthResponseDTO {
    String token;
    UserResponseDTO user;
}

