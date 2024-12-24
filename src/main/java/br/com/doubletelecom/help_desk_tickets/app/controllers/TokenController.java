package br.com.doubletelecom.help_desk_tickets.app.controllers;

import java.time.Instant;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.LoggedUserDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.LoginRequest;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.LoginResponse;
import br.com.doubletelecom.help_desk_tickets.app.domain.entity.ADPrincipal;
import br.com.doubletelecom.help_desk_tickets.app.domain.entity.Role;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.LoginUsernameOrPasswordException;
import br.com.doubletelecom.help_desk_tickets.app.repositories.UserRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.ldap.LDAPRepository;
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
    private final LDAPRepository ldapRepository;

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest loginReq){

        var user = userRep.findByUsername(loginReq.username());

        try {
            authenticate(loginReq.username(), loginReq.password());
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        var now = Instant.now();
        // Time in secounds to keep the token alive
        var expiresIn = 300L;
        // Retrive User Roles
        var scopes = user.get().getRoles().stream().map(Role::getName).collect(Collectors.joining(" "));

        var claims = JwtClaimsSet.builder()
                .issuer("AuthBackend")
                .subject(user.get().getId().toString())
                .issuedAt(now)
                .claim("scope", scopes)
                .expiresAt(now.plusSeconds(expiresIn));

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims.build())).getTokenValue();

        var loggedUser = new LoggedUserDto(user.get().getUsername());

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn, loggedUser));
    }

    private void authenticate(String username, String password) throws Exception {
		try {
			ADPrincipal principal = ldapRepository.findByCn(username);
			if(principal == null) {
				throw new LoginUsernameOrPasswordException();
			}
		} catch (DisabledException e) {
			throw new LoginUsernameOrPasswordException();
		} catch (BadCredentialsException e) {
			throw new LoginUsernameOrPasswordException();
		} catch(Exception e) {
			throw new LoginUsernameOrPasswordException();
			
		}
	}

}
