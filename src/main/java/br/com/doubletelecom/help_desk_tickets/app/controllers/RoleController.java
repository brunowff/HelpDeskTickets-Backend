package br.com.doubletelecom.help_desk_tickets.app.controllers;

import org.springframework.web.bind.annotation.RestController;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Role;
import br.com.doubletelecom.help_desk_tickets.app.services.RoleServices;
import br.com.doubletelecom.help_desk_tickets.app.services.UserServices;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController("/rm")
@AllArgsConstructor
public class RoleController {

    private final RoleServices roleServices;
    private final UserServices userServices;

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN', 'SCOPE_API_ROLE_MANAGER')")
    public ResponseEntity<List<Role>> findAll(JwtAuthenticationToken token) {
        var roles = roleServices.findAll();
        return ResponseEntity.ok(roles);
    }
    
    @GetMapping("/role/{roleName}/user/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN', 'SCOPE_API_ROLE_MANAGER')")
    public ResponseEntity<Void> addRole2User (@PathVariable("roleName") String roleName, @PathVariable("userId") String userId, JwtAuthenticationToken token) {
        userServices.addRoleToUser(roleName, userId, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/role/{roleName}/user/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN', 'SCOPE_API_ROLE_MANAGER')")
    public ResponseEntity<Void> removeRoleFromUser (@PathVariable("roleName") String roleName, @PathVariable("userId") String userId, JwtAuthenticationToken token) {
        userServices.removeRoleFromUser(roleName, userId, token);
        return ResponseEntity.ok().build();
    }
    
}
