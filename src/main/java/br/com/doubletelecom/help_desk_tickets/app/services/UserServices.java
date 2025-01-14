
/**
 * Interface for user-related services.
 * Provides methods for saving a user, retrieving all users, 
 * adding a role to a user, and removing a role from a user.
 * 
 * Methods:
 * - save(CreateUserDto userDto): Saves a new user based on the provided user data transfer object.
 * - updateUser(UserDto userDto, JwtAuthenticationToken token): Updates an existing user based on the provided user data transfer object.
 * - findAll(): Retrieves a list of all users.
 * - addRoleToUser(String userId, String roleId, JwtAuthenticationToken token): Adds a role to a user based on the provided user ID and role ID, with authentication.
 * - removeRoleFromUser(String userId, String roleId, JwtAuthenticationToken token): Removes a role from a user based on the provided user ID and role ID, with authentication.
 * 
 * @author
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateUserDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.UserDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemUserDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;

public interface UserServices {

    public User save(CreateUserDto userDto);
    public Page<PageItemUserDto> findAll(Pageable pageable);
    public User updateUser(UserDto userDto, JwtAuthenticationToken token);
    public Void passwordReset(UserDto userDto, JwtAuthenticationToken token);
    public Void activate(String userId, JwtAuthenticationToken token);
    public Void deactivate(String userId, JwtAuthenticationToken token);
    public Void addRoleToUser(String userId, String roleId, JwtAuthenticationToken token);
    public Void removeRoleFromUser(String userId, String roleId, JwtAuthenticationToken token);
    public Void addUserToGroup(String userId, String groupId, JwtAuthenticationToken token);
    public Void removeUserFromGroup(String userId, String groupId, JwtAuthenticationToken token);
    public List<PageItemGroupDto> findGroupsByUserId(String userId, JwtAuthenticationToken token);
    
}
