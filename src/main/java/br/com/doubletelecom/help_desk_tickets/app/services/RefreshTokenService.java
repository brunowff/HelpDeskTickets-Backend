package br.com.doubletelecom.help_desk_tickets.app.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.RefreshToken;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;

public interface RefreshTokenService {

    public RefreshToken saveRefreshToken(User user, UUID token, Instant expiresAt);
    public Boolean verifyExpiration(RefreshToken token);
    public Boolean isTokenValid(Jwt token);
    public Optional<RefreshToken> findByToken(UUID token);
    public void deleteByToken(UUID token);
    public void deleteByUser(User user);

}
