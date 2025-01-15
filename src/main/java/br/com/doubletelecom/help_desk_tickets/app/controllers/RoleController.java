/**
 * RoleController is a REST controller that provides endpoints for managing roles and their associations with users.
 * It includes endpoints to retrieve all roles, add a role to a user, and remove a role from a user.
 * 
 * Endpoints:
 * 
 * - GET /role-manager/roles - Retrieves all roles.</li>
 * - GET /role-manager/role/{roleName}/user/{userId} - Adds a role to a user.</li>
 * - DELETE /role-manager/role/{roleName}/user/{userId} - Removes a role from a user.</li>
 * 
 * 
 * Authorization:
 *   All endpoints require the caller to have either 'SCOPE_API_ADMIN' or 'SCOPE_API_ROLE_MANAGER' authority.</p>
 * 
 * Dependencies:
 * 
 * - RoleServices - Service for role-related operations.</li>
 * - UserServices - Service for user-related operations.</li>
 *
 * 
 * <p>Annotations:
 * 
 * - @RestController - Indicates that this class is a REST controller.</li>
 * - @RequestMapping("/role-manager") - Maps requests to /role-manager.</li>
 * - @AllArgsConstructor - Generates a constructor with one parameter for each field in the class.</li>
 *
 * 
 * @author 
 * @version
 */
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;




@RestController
@RequestMapping("/role-manager")
@AllArgsConstructor
public class RoleController {

    private final RoleServices roleServices;
    private final UserServices userServices;

    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_ROLE_MANAGER')")
    public ResponseEntity<List<Role>> findAll(JwtAuthenticationToken token) {
        var roles = roleServices.findAll();
        return ResponseEntity.ok(roles);
    }
    
    @PatchMapping("/roles/{roleName}/user/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_ROLE_MANAGER')")
    public ResponseEntity<Void> addRole2User (@PathVariable("roleName") String roleName, @PathVariable("userId") String userId, JwtAuthenticationToken token) {
        userServices.addRoleToUser(userId, roleName, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/roles/{roleName}/user/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_ROLE_MANAGER')")
    public ResponseEntity<Void> removeRoleFromUser (@PathVariable("roleName") String roleName, @PathVariable("userId") String userId, JwtAuthenticationToken token) {
        userServices.removeRoleFromUser(userId, roleName, token);
        return ResponseEntity.ok().build();
    }
    
}
