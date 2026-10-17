package com.dynamicbank.account.controller;

import com.dynamicbank.account.dto.CreateAccountRequestDTO;
import com.dynamicbank.account.dto.LoginRequestDTO;
import com.dynamicbank.account.dto.UserResponseDTO;
import com.dynamicbank.account.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final UserService service;

    public AccountController(UserService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createAccount(@Valid @RequestBody CreateAccountRequestDTO request) {
        return service.create(request);
    }

    @PostMapping("/login")
    public UserResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        return service.authenticate(request);
    }
}
