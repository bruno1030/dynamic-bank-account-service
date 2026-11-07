package com.dynamicbank.account.security;

import com.dynamicbank.account.model.Client;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Emite o JWT (assinado com HS256) devolvido no /create e no /login.
 * Claims: iss (emissor), sub (id do usuário), type, iat e exp.
 */
@Service
public class JwtTokenService {

    public static final String CLAIM_TYPE = "type";
    private static final String TYPE_CLIENT = "CLIENT";

    private final JwtEncoder jwtEncoder;
    private final JwtProperties properties;
    private final Clock clock;

    public JwtTokenService(JwtEncoder jwtEncoder, JwtProperties properties, Clock clock) {
        this.jwtEncoder = jwtEncoder;
        this.properties = properties;
        this.clock = clock;
    }

    public String generateToken(Client client) {
        Instant now = clock.instant();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(properties.getIssuer())
                .subject(client.getId().toString())
                .issuedAt(now)
                .expiresAt(now.plus(properties.getExpirationMinutes(), ChronoUnit.MINUTES))
                .claim(CLAIM_TYPE, TYPE_CLIENT)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}