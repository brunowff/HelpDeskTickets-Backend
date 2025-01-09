package br.com.doubletelecom.help_desk_tickets.app.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateUserDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemUserDto;
import br.com.doubletelecom.help_desk_tickets.app.services.UserServices;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;


@RestController("/profiles")
@AllArgsConstructor
public class UserController {

    private final UserServices userServices;

    @PostMapping("/users")
    public ResponseEntity<Void> createUser(@RequestBody @Valid CreateUserDto userDto){

        try {
            userServices.save(userDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

    }

    // Generate the User list if the requester is an admin.
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN')")
    public ResponseEntity<Page<PageItemUserDto>> listUsers(@PageableDefault(page = 0, size = 10, sort = {"username"}) Pageable pageable) {
        var users = userServices.findAll(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user/{id}/activate")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_USER_MANAGER')")
    public ResponseEntity<Void> activateUser(@PathVariable("id") String userId, JwtAuthenticationToken token) {
        userServices.activate(userId, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{id}/deactivate")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_USER_MANAGER')")
    public ResponseEntity<Void> deactivateUser(@PathVariable("id") String userId, JwtAuthenticationToken token) {
        userServices.deactivate(userId, token);
        return ResponseEntity.ok().build();
    }
       
}
