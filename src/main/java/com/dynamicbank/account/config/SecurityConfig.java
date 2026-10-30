package com.dynamicbank.account.config;

import com.dynamicbank.account.security.ApiKeyAuthenticationFilter;
import com.dynamicbank.account.security.ApiKeyProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(ApiKeyProperties.class)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   ApiKeyProperties apiKeyProperties) throws Exception {
        http
                // API stateless, sem cookies: CSRF não se aplica aqui (na webapp ele continua habilitado).
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Sem autenticação -> 401 (e não 403).
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .addFilterBefore(new ApiKeyAuthenticationFilter(apiKeyProperties), AuthorizationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/accounts/create", "/accounts/login")
                        .hasAuthority(ApiKeyAuthenticationFilter.AUTHORITY_WEBAPP)
                        .requestMatchers(HttpMethod.POST, "/accounts/update-saldos")
                        .hasAuthority(ApiKeyAuthenticationFilter.AUTHORITY_TRANSFER)
                        // Demais rotas: bloqueadas até configurarmos o JWT.
                        .anyRequest().authenticated());

        return http.build();
    }
}