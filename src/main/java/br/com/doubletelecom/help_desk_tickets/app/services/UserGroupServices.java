/**
 * Service interface for managing user groups.
 * Provides methods for creating, retrieving, updating, and deleting user groups,
 * as well as adding and removing users from groups.
 * 
 * Methods:
 * - save(CreateUserGroupDto UserGroupDto, JwtAuthenticationToken token): Saves a new user group.
 * - findById(String UserGroupId, JwtAuthenticationToken token): Finds a user group by its ID.
 * - delete(String UserGroupId, JwtAuthenticationToken token): Deletes a user group by its ID.
 * - addUsertToGroup(String userId, String groupId, JwtAuthenticationToken token): Adds a user to a group.
 * - removeUserFromGroup(String userId, String groupId, JwtAuthenticationToken token): Removes a user from a group.
 * - findAll(): Retrieves all user groups.
 * - findUsersByGroupId(String groupId, JwtAuthenticationToken token): Finds all users in a specific group.
 * - findGroupsByUserId(String userId, JwtAuthenticationToken token): Finds all groups a specific user belongs to.
 * 
 * @author
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.services;

import java.util.List;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateUserGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.UserGroup;

public interface UserGroupServices {
    
    public UserGroup save(CreateUserGroupDto UserGroupDto, JwtAuthenticationToken token);
    public UserGroup findById(String UserGroupId, JwtAuthenticationToken token);
    public Void delete(String UserGroupId, JwtAuthenticationToken token);
    public List<UserGroup> findAll();

}
