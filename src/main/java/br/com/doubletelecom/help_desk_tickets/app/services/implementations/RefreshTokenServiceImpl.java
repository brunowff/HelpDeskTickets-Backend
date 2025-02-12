package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.RefreshToken;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;
import br.com.doubletelecom.help_desk_tickets.app.repositories.RefreshTokenRepository;
import br.com.doubletelecom.help_desk_tickets.app.services.RefreshTokenService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRep;

    @Override
    public RefreshToken saveRefreshToken(User user, UUID token, Instant expiresAt) {
        var refreshToken = new RefreshToken();
        refreshToken.setToken(token);
        refreshToken.setExpiresAt(expiresAt);
        refreshToken.setUser(user);
        return refreshTokenRep.save(refreshToken);
    }

    @Override
    public Boolean verifyExpiration(RefreshToken token) {
        if(token.getExpiresAt().compareTo(Instant.now()) < 0){
            refreshTokenRep.deleteByToken(token.getToken());
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Optional<RefreshToken> findByToken(UUID token) {
        return refreshTokenRep.findByToken(token);
    }

    @Override 
    public void deleteByToken(UUID token) {
        refreshTokenRep.deleteByToken(token);
    }

}
