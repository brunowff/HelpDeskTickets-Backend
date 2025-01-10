/**
 * UserController is a REST controller that manages user-related operations.
 * It provides endpoints for creating users, listing users, and activating/deactivating users.
 * 
 * Endpoints:
 * 
 * - POST /profiles-manager/users: Creates a new user.
 * - GET /profiles-manager/users: Lists users with pagination, accessible only to admins.
 * - GET /profiles-manager/user/{id}/activate: Activates a user by ID, accessible to admins and user managers.
 * - GET /profiles-manager/user/{id}/deactivate: Deactivates a user by ID, accessible to admins and user managers.
 * 
 * This controller uses Spring Security annotations to restrict access to certain endpoints based on user roles.
 * 
 * @author 
 * @version 
 */

package br.com.doubletelecom.help_desk_tickets.app.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateUserDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemUserDto;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.ObjectNotProcessableException;
import br.com.doubletelecom.help_desk_tickets.app.services.UserServices;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;



@RestController("/profiles-manager")
@AllArgsConstructor
public class UserController {

    private final UserServices userServices;

    @PostMapping("/users")
    public ResponseEntity<PageItemUserDto> createUser(@RequestBody @Valid CreateUserDto userDto, UriComponentsBuilder uriBuilder){

        try {
            var user = userServices.save(userDto);
            var uri = uriBuilder.path("/users/{id}").buildAndExpand(user.getUserId()).toUri();
            return ResponseEntity.created(uri).body(new PageItemUserDto(user));
        } catch (Exception e) {
           throw new ObjectNotProcessableException();
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
