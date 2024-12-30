/**
 * Service interface for managing users.
 * Provides methods for saving a new user and retrieving all users.
 */
package br.com.doubletelecom.help_desk_tickets.app.services;

import java.util.List;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateUserDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;

public interface UserServices {

    public User save(CreateUserDto userDto);
    public List<User> findAll();
    public Void addRoleToUser(String userId, String roleId, JwtAuthenticationToken token);
    public Void removeRoleFromUser(String userId, String roleId, JwtAuthenticationToken token);
    
}
