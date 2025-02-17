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

    public String generateAccessToken(User user, JwtEncoder jwtEncoder, Instant expiresAt) {
        var scopes = user.getRoles().stream().map(Role::getName).collect(Collectors.joining(" "));
        return createToken("AuthBackend - AccessToken", user.getUserId().toString(), scopes, expiresAt, jwtEncoder);
    }

    public String generateRefreshToken(User user, UUID token, JwtEncoder jwtEncoder, Instant expiresAt) {
        var scopes = token.toString();
        return createToken("AuthBackend - RefreshToken", user.getUserId().toString(), scopes, expiresAt, jwtEncoder);
    }

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

    public UUID getRefreshTokenUUID(Jwt token) {
        var tokenUUID = UUID.fromString(token.getClaim("scope").toString());
        return tokenUUID;
    }

    public Boolean verifyExpiration(Jwt token) {
        var expiration = token.getExpiresAt();
        if(expiration == null || expiration.compareTo(Instant.now()) < 0){
            return false;
        } else {
            return true;
        }
    }

}