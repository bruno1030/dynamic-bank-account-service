package com.dynamicbank.account.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;

/**
 * Confere o cabeçalho X-API-Key nas rotas protegidas por chave de serviço.
 * Se a chave estiver certa para aquela rota, autentica o chamador (webapp ou TransferService)
 * com uma authority própria. Se estiver ausente ou errada, responde 401.
 *
 * Atenção: esta classe NÃO é um @Component. Ela é criada na SecurityConfig, para rodar
 * só dentro da cadeia do Spring Security (senão o Spring Boot a registraria duas vezes).
 */
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

    public static final String HEADER = "X-API-Key";
    public static final String AUTHORITY_WEBAPP = "SERVICE_WEBAPP";
    public static final String AUTHORITY_TRANSFER = "SERVICE_TRANSFER";

    private record Credential(String callerName, String key, String authority) {
    }

    private final Map<String, Credential> credentialsByPath;

    public ApiKeyAuthenticationFilter(ApiKeyProperties properties) {
        Credential webapp = new Credential("webapp", properties.webapp(), AUTHORITY_WEBAPP);
        Credential transfer = new Credential("transfer-service", properties.transfer(), AUTHORITY_TRANSFER);

        this.credentialsByPath = Map.of(
                "/accounts/create", webapp,
                "/accounts/login", webapp,
                "/accounts/update-saldos", transfer
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Credential expected = credentialsByPath.get(request.getRequestURI());

        // Rota que não usa API key: segue o fluxo normal (JWT, mais adiante).
        if (expected == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String provided = request.getHeader(HEADER);
        if (provided == null || !sameKey(provided, expected.key())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                expected.callerName(),
                null,
                List.of(new SimpleGrantedAuthority(expected.authority())));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        filterChain.doFilter(request, response);
    }

    // Comparação em tempo constante, para não vazar informação pelo tempo de resposta.
    private boolean sameKey(String provided, String expected) {
        return MessageDigest.isEqual(
                provided.getBytes(StandardCharsets.UTF_8),
                expected.getBytes(StandardCharsets.UTF_8));
    }
}