/**
 * Utilitário para geração e validação de tokens JWT com algoritmo RS256.
 *
 * <p><b>Access Token</b>: o claim {@code scope} contém os nomes das roles do usuário
 * separados por espaço (ex: {@code "API_ADMIN API_BASIC"}). O Spring Security interpreta
 * cada escopo como uma authority {@code SCOPE_<ROLE>}, usada nas anotações {@code @PreAuthorize}.
 *
 * <p><b>Refresh Token</b>: o claim {@code scope} contém um UUID aleatório que é persistido
 * em {@code tb_refresh_tokens}. Isso permite revogar o token no logout sem precisar de
 * uma blacklist de JWTs.
 *
 * <p>A assinatura usa o par de chaves RSA configurado em {@code jwt.public.key} e
 * {@code jwt.private.key} (via {@code SecurityConfig}).
 */
package br.com.doubletelecom.help_desk_tickets.app.security;

import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Role;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;

@Component
public class JWTUtils{

    /**
     * Gera um access token JWT para o usuário.
     * O claim {@code scope} contém as roles do usuário separadas por espaço,
     * que o Spring Security converte em authorities {@code SCOPE_<ROLE>}.
     *
     * @param user       usuário autenticado
     * @param jwtEncoder encoder RSA injetado via SecurityConfig
     * @param expiresAt  instante de expiração (now + accessTokenTTL)
     * @return JWT assinado em formato compacto
     */
    public String generateAccessToken(User user, JwtEncoder jwtEncoder, Instant expiresAt) {
        var scopes = user.getRoles().stream().map(Role::getName).collect(Collectors.joining(" "));
        return createToken("AuthBackend - AccessToken", user.getUserId().toString(), scopes, expiresAt, jwtEncoder);
    }

    /**
     * Gera um refresh token JWT para o usuário.
     * O claim {@code scope} contém o UUID do registro em {@code tb_refresh_tokens},
     * permitindo validação e revogação sem blacklist.
     *
     * @param user       usuário autenticado
     * @param token      UUID aleatório que será persistido no banco
     * @param jwtEncoder encoder RSA injetado via SecurityConfig
     * @param expiresAt  instante de expiração (now + refreshTokenTTL)
     * @return JWT assinado em formato compacto
     */
    public String generateRefreshToken(User user, UUID token, JwtEncoder jwtEncoder, Instant expiresAt) {
        var scopes = token.toString();
        return createToken("AuthBackend - RefreshToken", user.getUserId().toString(), scopes, expiresAt, jwtEncoder);
    }

    /**
     * Constrói e assina um JWT com os claims fornecidos.
     *
     * @param issuer     identificador do emissor (diferencia access de refresh token)
     * @param subject    ID do usuário (UUID como string)
     * @param scopes     conteúdo do claim {@code scope}
     * @param expiresAt  instante de expiração
     * @param jwtEncoder encoder RSA
     * @return JWT assinado em formato compacto (header.payload.signature)
     */
    public String createToken(String issuer, String subject, String scopes, Instant expiresAt, JwtEncoder jwtEncoder) {
        var now = Instant.now();
        var claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .subject(subject)
                .issuedAt(now)
                .claim("scope", scopes)
                .expiresAt(expiresAt);

        return jwtEncoder.encode(JwtEncoderParameters.from(claims.build())).getTokenValue();
    }

    /**
     * Extrai o UUID do refresh token a partir do claim {@code scope} do JWT decodificado.
     * Esse UUID é usado para localizar e validar o registro em {@code tb_refresh_tokens}.
     *
     * @param token JWT de refresh já decodificado
     * @return UUID do refresh token persistido no banco
     */
    public UUID getRefreshTokenUUID(Jwt token) {
        var tokenUUID = UUID.fromString(token.getClaim("scope").toString());
        return tokenUUID;
    }

    /**
     * Verifica se o JWT ainda está dentro do prazo de validade.
     *
     * @param token JWT decodificado
     * @return {@code true} se não expirou, {@code false} caso contrário
     */
    public Boolean verifyExpiration(Jwt token) {
        var expiration = token.getExpiresAt();
        if(expiration == null || expiration.compareTo(Instant.now()) < 0){
            return false;
        } else {
            return true;
        }
    }

}