package com.dynamicbank.account.service;

import com.dynamicbank.account.dto.*;
import com.dynamicbank.account.kafka.AuditEventDTO;
import com.dynamicbank.account.kafka.AuditProducer;
import com.dynamicbank.account.kafka.EventTypeEnum;
import com.dynamicbank.account.model.Account;
import com.dynamicbank.account.model.AccountStatus;
import com.dynamicbank.account.model.Client;
import com.dynamicbank.account.repository.AccountRepository;
import com.dynamicbank.account.repository.UserRepository;
import com.dynamicbank.account.security.JwtTokenService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AuditProducer auditProducer;
    private final ObjectMapper objectMapper;

    private static final BigDecimal INITIAL_BALANCE = new BigDecimal("1000.00");
    private static final int MINIMUM_AGE = 18;
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private static final String DUPLICATE_MESSAGE = "Email ou username já cadastrado";

    public AccountService(AccountRepository accountRepository,
                          UserRepository userRepository, AuditProducer auditProducer, ObjectMapper objectMapper,
                          PasswordEncoder passwordEncoder,
                          JwtTokenService jwtTokenService){
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.auditProducer = auditProducer;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenService = jwtTokenService;
    }

    @Transactional
    public AuthResponseDTO create(CreateAccountRequestDTO request) {
        validateAge(request.getBirthDate());

        String email = request.getEmail().trim().toLowerCase(Locale.ROOT);
        String username = request.getUsername().trim();

        if (userRepository.existsByEmail(email) || userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, DUPLICATE_MESSAGE);
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
            // Corrida entre dois cadastros simultâneos: o UNIQUE do banco é a barreira final.
            throw new ResponseStatusException(HttpStatus.CONFLICT, DUPLICATE_MESSAGE);
        }

        Account account = new Account(generateAccountNumber(), client, INITIAL_BALANCE);
        accountRepository.save(account);

        String accountNumber = account.getAccountNumber();
        UUID clientId = account.getClient().getId();
        AuditEventDTO payloadDto = new AuditEventDTO(accountNumber, clientId, EventTypeEnum.ABERTURA_DE_CONTA);
        String payloadJson = objectMapper.writeValueAsString(payloadDto);

        // publish message on Kafka topic
        auditProducer.sendEvent(null, payloadJson);

        String token = jwtTokenService.generateToken(client);

        return new AuthResponseDTO(token, new UserResponseDTO(
                client.getId(), client.getFullName(), client.getUsername(), client.getEmail()));
    }

    private void validateAge(LocalDate birthDate) {
        if (Period.between(birthDate, LocalDate.now()).getYears() < MINIMUM_AGE) {
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
