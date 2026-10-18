package com.dynamicbank.account.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserResponseDTO {

    private final UUID id;

    private final String fullName;

    private final String username;

    private final String email;

}