package com.dynamicbank.account.service;

import com.dynamicbank.account.dto.*;
import com.dynamicbank.account.model.Account;
import com.dynamicbank.account.model.AccountStatus;
import com.dynamicbank.account.model.Client;
import com.dynamicbank.account.repository.AccountRepository;
import com.dynamicbank.account.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("1000.00");
    private static final int MINIMUM_AGE = 18;
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /*@Transactional
    public AuthResponseDTO create(CreateAccountRequestDTO request) {
        validateAge(request.getBirthDate());

        String email = request.getEmail().trim().toLowerCase();
        String username = request.getUsername().trim();

        if (userRepository.existsByEmail(email) || userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ou username já cadastrado");
        }

        Client client = new Client(
                request.getFullName().trim(),
                username,
                email,
                passwordEncoder.encode(request.getPassword()),
                request.getBirthDate());

        try {
            userRepository.saveAndFlush(client);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ou username já cadastrado");
        }

        Account account = new Account(generateAccountNumber(), client, INITIAL_BALANCE);
        accountRepository.save(account);

        String token = jwtTokenService.generateToken(client);
        auditEventPublisher.publishAccountOpened(client.getId());

        return new AuthResponseDTO(token, new UserResponseDTO(
                client.getId(), client.getFullName(), client.getUsername(), client.getEmail()));
    }*/

    private void validateAge(LocalDate birthDate) {
        if (Period.between(birthDate, LocalDate.now(clock)).getYears() < MINIMUM_AGE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Idade mínima de 18 anos");
        }
    }

    private String generateAccountNumber() {
        String number;
        do {
            StringBuilder sb = new StringBuilder(8);
            for (int i = 0; i < 8; i++) {
                sb.append(ALPHANUMERIC.charAt(RANDOM.nextInt(ALPHANUMERIC.length())));
            }
            number = sb.toString();
        } while (accountRepository.existsByAccountNumber(number));
        return number;
    }

    public AuthResponseDTO authenticate(@Valid LoginRequestDTO request) {
        return null;
    }

    public void closeAccount(UUID uuid) {

    }

    public List<AccountSearchResultDTO> searchAccounts(UUID uuid, @Valid SearchAccountsRequestDTO request) {
        return null;
    }

    public void updateBalances(@Valid UpdateBalancesRequestDTO request) {

    }

    @Transactional(readOnly = true)
    public AccountResponseDTO getAccount(UUID uuid) {
        Account account = accountRepository.findByClientId(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Conta não encontrada"));

        if (account.getStatus() == AccountStatus.CLOSED) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Conta encerrada");
        }

        return new AccountResponseDTO(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getCurrency(),
                account.getStatus().name()
        );
    }
}
