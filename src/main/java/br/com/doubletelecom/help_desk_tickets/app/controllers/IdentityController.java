package br.com.doubletelecom.help_desk_tickets.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.ADUser;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.AuthenticationRequest;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.AuthenticationResponse;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.EntityResponse;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.UserRegisterRequestDTO;
import br.com.doubletelecom.help_desk_tickets.app.repositories.ldap.LDAPRepository;
import br.com.doubletelecom.help_desk_tickets.app.security.JWTTokenUtil;
import br.com.doubletelecom.help_desk_tickets.app.services.UserService;


@RestController
@RequestMapping("identity")
public class IdentityController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserService userService;

	@Autowired
	private JWTTokenUtil jwtTokenUtil;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private LDAPRepository ldaPrincipalRepository;

	@PostMapping("/token")
	public ResponseEntity<Object> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {

		try {
			authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		} catch (Exception e) {
			return EntityResponse.generateResponse("Authentication", HttpStatus.UNAUTHORIZED,
					"Invalid credentials, please check details and try again.");
		}
		final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);
		final String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails);

		return EntityResponse.generateResponse("Authentication", HttpStatus.OK,
				new AuthenticationResponse(token, refreshToken));

	}

	private void authenticate(String username, String password) throws Exception {
		try {
			ADUser principal = ldaPrincipalRepository.findByCn(username);
			if(principal == null) {
				throw new Exception("Invalid credentials");
			}
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}catch(Exception e) {
			throw new Exception("INVALID_CREDENTIALS", e.getCause());
			
		}
	}
	
	@PostMapping("register")
	public ResponseEntity<Object> register(@RequestBody UserRegisterRequestDTO request){
		request.setPassword(passwordEncoder.encode(request.getPassword()));
		return EntityResponse.generateResponse("Regsiter User", HttpStatus.OK, userService.createUser(request));
	}

	@GetMapping("profile")
	public ResponseEntity<Object> retrieveUserProfile(){
		return EntityResponse.generateResponse("User Profile", HttpStatus.OK, userService.findCurrentUser());
	}
}
