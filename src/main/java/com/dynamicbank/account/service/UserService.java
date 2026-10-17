package com.dynamicbank.account.service;

import com.dynamicbank.account.dto.CreateAccountRequestDTO;
import com.dynamicbank.account.dto.LoginRequestDTO;
import com.dynamicbank.account.dto.UserResponseDTO;
import com.dynamicbank.account.model.User;
import com.dynamicbank.account.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Transactional
    public UserResponseDTO create(CreateAccountRequestDTO req) {
        if (repository.existsByUsername(req.getUsername()) || repository.existsByEmail(req.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username ou email já cadastrado");
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPasswordHash(encoder.encode(req.getPassword()));
        User saved = repository.save(user);
        return new UserResponseDTO(saved.getId(), saved.getUsername(), saved.getEmail());
    }

    @Transactional(readOnly = true)
    public UserResponseDTO authenticate(LoginRequestDTO req) {
        User account = repository
                .findByUsernameOrEmail(req.getUsernameOrEmail(), req.getUsernameOrEmail())
                .filter(a -> encoder.matches(req.getPassword(), a.getPasswordHash()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));
        return new UserResponseDTO(account.getId(), account.getUsername(), account.getEmail());
    }
}