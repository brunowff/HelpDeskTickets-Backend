/**
 * Controller for handling JWT token generation and user login.
 * 
 * @author 
 * @version
 * 
 */
package br.com.doubletelecom.help_desk_tickets.app.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.LoginRequest;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.LoginResponse;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.RefreshTokenRequestDto;
import br.com.doubletelecom.help_desk_tickets.app.services.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

/*
 * JWT Token generation 
 */
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest loginReq){
        return ResponseEntity.ok(authenticationService.login(loginReq));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(@Validated @RequestBody RefreshTokenRequestDto refreshTokenRequestDto){
        return ResponseEntity.ok(authenticationService.refresh(refreshTokenRequestDto.refreshToken()));
    }

}
