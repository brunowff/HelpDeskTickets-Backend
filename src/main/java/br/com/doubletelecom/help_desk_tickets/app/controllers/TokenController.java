/**
 * Controller for handling JWT token generation and user login.
 * 
 * @author 
 * @version
 * 
 */
package br.com.doubletelecom.help_desk_tickets.app.controllers;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.LoggedUserDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.LoginRequest;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.LoginResponse;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Role;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.LoginEmailOrPasswordException;
import br.com.doubletelecom.help_desk_tickets.app.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

/*
 * JWT Token generation 
 */
@RestController
@AllArgsConstructor
public class TokenController {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRep;
    private final BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest loginReq){

        var user = userRep.findByEmail(loginReq.email());

        if(user.isEmpty() || !user.get().isLoginCorrect(loginReq, passwordEncoder)){
            throw new LoginEmailOrPasswordException();
        }

        var now = Instant.now();
        // Time in secounds to keep the token alive
        var expiresIn = 300L;
        // Retrive User Roles
        var scopes = user.get().getRoles().stream().map(Role::getName).collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("AuthBackend")
                .subject(user.get().getUserId().toString())
                .issuedAt(now)
                .claim("scope", scopes)
                .expiresAt(now.plusSeconds(expiresIn));

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims.build())).getTokenValue();

        var loggedUser = new LoggedUserDto(user.get().getUserId(), user.get().getUsername(), user.get().getFullname(), user.get().getEmail());

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn, loggedUser));
    }

}
