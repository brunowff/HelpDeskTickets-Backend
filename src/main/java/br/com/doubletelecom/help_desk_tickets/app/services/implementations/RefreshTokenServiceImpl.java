package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.RefreshToken;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.TokenExpiredException;
import br.com.doubletelecom.help_desk_tickets.app.repositories.RefreshTokenRepository;
import br.com.doubletelecom.help_desk_tickets.app.security.JWTUtils;
import br.com.doubletelecom.help_desk_tickets.app.services.RefreshTokenService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRep;
    private final JWTUtils jwtUtils;

    @Override
    @Transactional
    public RefreshToken saveRefreshToken(User user, UUID token, Instant expiresAt) {
        var refreshToken = refreshTokenRep.findByUser(user).orElse(new RefreshToken());
        refreshToken.setToken(token);
        refreshToken.setExpiresAt(expiresAt);
        refreshToken.setUser(user);
        return refreshTokenRep.save(refreshToken);
    }

    @Override
    @Transactional
    public Boolean verifyExpiration(RefreshToken token) {
        if(token.getExpiresAt().compareTo(Instant.now()) < 0){
            refreshTokenRep.deleteByToken(token.getToken());
            throw new TokenExpiredException();
        } else {
            return true;
        }
    }

    @Override
    @Transactional
    public Boolean isTokenValid(Jwt token) {
        var refreshToken = refreshTokenRep.findByToken(jwtUtils.getRefreshTokenUUID(token)).orElseThrow(() -> new TokenExpiredException());
        return verifyExpiration(refreshToken);
    }

    @Override
    @Transactional
    public Optional<RefreshToken> findByToken(UUID token) {
        return refreshTokenRep.findByToken(token);
    }

    @Override
    @Transactional
    public void deleteByToken(UUID token) {
        refreshTokenRep.deleteByToken(token);
    }
    
    @Override
    @Transactional
    public void deleteByUser(User user) {
        refreshTokenRep.deleteByUser(user);
    }
}
