package com.dynamicbank.account.dto;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class UserResponseDTO {

    UUID id;

    String username;

    String email;

}
