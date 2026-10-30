package com.dynamicbank.account.security;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Chaves de API esperadas pelo AccountService, lidas do application.properties:
 *   security.api-keys.webapp   -> usada pela webapp em /create e /login
 *   security.api-keys.transfer -> usada pelo TransferService em /update-saldos
 */
@Validated
@ConfigurationProperties(prefix = "security.api-keys")
public record ApiKeyProperties(
        @NotBlank String webapp,
        @NotBlank String transfer
) {
}