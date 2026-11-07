package com.dynamicbank.account.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Configuração do JWT, lida do application.properties:
 *   jwt.secret              -> segredo compartilhado (mínimo 32 caracteres, exigência do HS256)
 *   jwt.expiration-minutes  -> validade do token (padrão: 30)
 *   jwt.issuer              -> emissor, claim "iss" (padrão: dynamic-bank-account)
 */
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    @NotBlank
    @Size(min = 32)
    private String secret;

    @Positive
    private long expirationMinutes = 30;

    @NotBlank
    private String issuer = "dynamic-bank-account";

}
