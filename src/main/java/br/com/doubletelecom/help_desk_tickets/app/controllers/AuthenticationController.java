/**
 * Controller for handling JWT token generation and user login.
 * 
 * @author 
 * @version
 * 
 */
package br.com.doubletelecom.help_desk_tickets.app.controllers;

import java.time.Instant;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.LoggedUserDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.LoginRequest;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.LoginResponse;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.RefreshTokenRequestDto;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.LoginEmailOrPasswordException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.TokenExpiredException;
import br.com.doubletelecom.help_desk_tickets.app.security.JWTUtils;
import br.com.doubletelecom.help_desk_tickets.app.services.RefreshTokenService;
import br.com.doubletelecom.help_desk_tickets.app.services.UserServices;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

/*
 * JWT Token generation 
 */
@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {

    private final JwtEncoder jwtEncoder;
    private final UserServices userService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;
    private final Long accessTokenExpiresIn = 60L;
    private final Instant accessTokenExpiresAt = Instant.now().plusSeconds(accessTokenExpiresIn);
    private final Long refreshTokenExpiresIn = 900L;
    private final Instant refreshTokenExpiresAt = Instant.now().plusSeconds(refreshTokenExpiresIn);

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest loginReq){

        var user = userService.findByEmail(loginReq.email()).orElseThrow( () -> new LoginEmailOrPasswordException());
        
        if(!user.isLoginCorrect(loginReq, passwordEncoder) || user.getActive() == false){
            throw new LoginEmailOrPasswordException();
        }
        var tokenUUID = UUID.randomUUID();
        var accessToken = JWTUtils.generateAccessToken(user, jwtEncoder, this.accessTokenExpiresAt);
        refreshTokenService.saveRefreshToken(user, tokenUUID, refreshTokenExpiresAt);
        var refreshToken = JWTUtils.generateRefreshToken(user, tokenUUID, jwtEncoder, this.refreshTokenExpiresAt);
        var loggedUser = new LoggedUserDto(user.getUserId(), user.getUsername(), user.getFullname(), user.getEmail());

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken, Instant.now().plusSeconds(accessTokenExpiresIn), loggedUser));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@Validated @RequestBody RefreshTokenRequestDto refreshTokenRequestDto){
        //TODO - Extract Claims From JwtRefreshToken and verify token UUID
        var refreshTokenRequest = refreshTokenService.findByToken(UUID.fromString(refreshTokenRequestDto.refreshToken())).orElseThrow(() -> new TokenExpiredException());
        if(refreshTokenService.verifyExpiration(refreshTokenRequest) == false){
            throw new TokenExpiredException();
        }
        var tokenUUID = UUID.randomUUID();
        var user = refreshTokenRequest.getUser();
        var accessToken = JWTUtils.generateAccessToken(user, jwtEncoder, this.accessTokenExpiresAt);
        var refreshToken = JWTUtils.generateRefreshToken(user, tokenUUID, jwtEncoder, this.refreshTokenExpiresAt);
        var loggedUser = new LoggedUserDto(user.getUserId(), user.getUsername(), user.getFullname(), user.getEmail());

        refreshTokenService.saveRefreshToken(user, tokenUUID, refreshTokenExpiresAt);
        refreshTokenService.deleteByToken(UUID.fromString(refreshTokenRequestDto.refreshToken()));

        return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken, Instant.now().plusSeconds(accessTokenExpiresIn), loggedUser));
    }

}
