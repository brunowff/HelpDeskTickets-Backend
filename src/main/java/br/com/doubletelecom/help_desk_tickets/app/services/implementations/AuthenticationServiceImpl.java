package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.time.Instant;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Service;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.LoggedUserDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.LoginRequest;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.LoginResponse;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.LoginEmailOrPasswordException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.TokenExpiredException;
import br.com.doubletelecom.help_desk_tickets.app.security.JWTUtils;
import br.com.doubletelecom.help_desk_tickets.app.services.AuthenticationService;
import br.com.doubletelecom.help_desk_tickets.app.services.RefreshTokenService;
import br.com.doubletelecom.help_desk_tickets.app.services.UserServices;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    private final UserServices userService;
    private final RefreshTokenService refreshTokenService;
    private final JWTUtils jwtUtils;
    private final Long accessTokenTTL;
    private final Long refreshTokenTTL;
    
    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginReq) {
        var user = userService.findByEmail(loginReq.email()).orElseThrow( () -> new LoginEmailOrPasswordException());
        
        if(!user.isLoginCorrect(loginReq, passwordEncoder) || user.getActive() == false){
            throw new LoginEmailOrPasswordException();
        }

        var now = Instant.now();
        var accessTokenExpiresAt = now.plusSeconds(accessTokenTTL);
        var refreshTokenExpiresAt = now.plusSeconds(refreshTokenTTL);

        var tokenUUID = UUID.randomUUID();

        var accessToken = jwtUtils.generateAccessToken(user, jwtEncoder, accessTokenExpiresAt);
        var refreshToken = jwtUtils.generateRefreshToken(user, tokenUUID, jwtEncoder, refreshTokenExpiresAt);
        var loggedUser = new LoggedUserDto(user.getUserId(), user.getUsername(), user.getFullname(), user.getEmail());
        refreshTokenService.deleteByUser(user);
        refreshTokenService.saveRefreshToken(user, tokenUUID, refreshTokenExpiresAt);

        return new LoginResponse(accessToken, accessTokenExpiresAt, refreshToken, refreshTokenExpiresAt, loggedUser);
    }

    @Override
    @Transactional
    public LoginResponse refresh(String refreshToken) {
        
        var jwtRefreshToken = jwtDecoder.decode(refreshToken);
        var user = userService.findByUserId(UUID.fromString(jwtRefreshToken.getSubject())).orElseThrow( () -> new LoginEmailOrPasswordException());
        
        if(!refreshTokenService.isTokenValid(jwtRefreshToken)){
            throw new TokenExpiredException();
        }

        var now = Instant.now();
        var accessTokenExpiresAt = now.plusSeconds(accessTokenTTL);
        var refreshTokenExpiresAt = now.plusSeconds(refreshTokenTTL);

        var tokenUUID = UUID.randomUUID();

        var accessToken = jwtUtils.generateAccessToken(user, jwtEncoder, accessTokenExpiresAt);
        var refreshTokenResponse = jwtUtils.generateRefreshToken(user, tokenUUID, jwtEncoder, refreshTokenExpiresAt);
        var loggedUser = new LoggedUserDto(user.getUserId(), user.getUsername(), user.getFullname(), user.getEmail());
        refreshTokenService.deleteByUser(user);
        refreshTokenService.saveRefreshToken(user, tokenUUID, refreshTokenExpiresAt);
        
        return new LoginResponse(accessToken, accessTokenExpiresAt, refreshTokenResponse, refreshTokenExpiresAt, loggedUser);

    }

    @Override
    @Transactional
    public Void logout(String refreshToken) {
        var jwtRefreshToken = jwtDecoder.decode(refreshToken);
        refreshTokenService.deleteByToken(jwtUtils.getRefreshTokenUUID(jwtRefreshToken));
        return null;
    }

}
