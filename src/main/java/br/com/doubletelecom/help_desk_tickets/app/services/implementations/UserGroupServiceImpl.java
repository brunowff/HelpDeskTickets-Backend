/**
 * Service implementation for managing user groups.
 * This service provides methods to save, find, delete, and manage user groups.
 * It also includes methods to add and remove users from groups, and to find users by group ID and groups by user ID.
 * 
 * Dependencies:
 * - UserRepository: Repository for managing users.
 * - GroupRepository: Repository for managing groups.
 * - UserGroupRepository: Repository for managing user groups.
 * 
 * Methods:
 * - save(CreateUserGroupDto userGroupDto, JwtAuthenticationToken token): Saves a new user group.
 * - findById(String UserGroupId, JwtAuthenticationToken token): Finds a user group by its ID.
 * - findAll(): Finds all user groups.
 * - delete(String UserGroupId, JwtAuthenticationToken token): Deletes a user group by its ID.
 * - addUsertToGroup(String userId, String groupId, JwtAuthenticationToken token): Adds a user to a group.
 * - removeUserFromGroup(String userId, String groupId, JwtAuthenticationToken token): Removes a user from a group.
 * - findUsersByGroupId(String groupId, JwtAuthenticationToken token): Finds users by group ID.
 * - findGroupsByUserId(String userId, JwtAuthenticationToken token): Finds groups by user ID.
 * 
 * Exceptions:
 * - UserNotFoundException: Thrown when a user is not found.
 * - ObjectNotFoundException: Thrown when an object is not found.
 * - ObjectNotProcessableException: Thrown when an object cannot be processed.
 * - UserNotAuthorizedException: Thrown when a user is not authorized to perform an action.
 * 
 * Annotations:
 * - @Service: Indicates that this class is a service.
 * - @AllArgsConstructor: Generates a constructor with one parameter for each field in the class.
 * - @Transactional: Indicates that the methods should be executed within a transaction.
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateUserGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.UserGroup;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.ObjectNotFoundException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.ObjectNotProcessableException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.UserNotAuthorizedException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.UserNotFoundException;
import br.com.doubletelecom.help_desk_tickets.app.repositories.GroupRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.UserGroupRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.UserRepository;
import br.com.doubletelecom.help_desk_tickets.app.services.UserGroupServices;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserGroupServiceImpl implements UserGroupServices{
    
    private final UserRepository userRep;
    private final GroupRepository groupRep;
    private final UserGroupRepository userGroupRep;

    @Override
    @Transactional
    public UserGroup save(@RequestBody @Valid CreateUserGroupDto userGroupDto, JwtAuthenticationToken token) {

        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow(() -> new UserNotFoundException());
        var userToAdd = userRep.findById(userGroupDto.userId()).orElseThrow(() -> new UserNotFoundException());
        
        if(userToAdd.hasGroup(userGroupDto.groupId())){
            throw new ObjectNotProcessableException();
        }

        if(user.isAdmin() || user.hasRole("API_GROUP_MANAGER")){
            var group = groupRep.findById(userGroupDto.groupId()).orElseThrow( () -> new ObjectNotFoundException());

            try {
                var userGroup = new UserGroup();
                userGroup.setUser(user);
                userGroup.setGroup(group);
                userGroupRep.save(userGroup);
                return userGroup;
            } catch (Exception e) {
                throw new ObjectNotProcessableException();
            }
        } else {
            throw new UserNotAuthorizedException();
        }
    }

    @Override
    @Transactional
    public UserGroup findById(@RequestParam String UserGroupId, JwtAuthenticationToken token) {
        var userGroup = userGroupRep.findById(UUID.fromString(UserGroupId)).orElseThrow( () -> new ObjectNotFoundException());
        return userGroup;
    }

    @Override
    @Transactional
    public List<UserGroup> findAll() {
        var userGroups = userGroupRep.findAll();
        return userGroups;
    }

    @Override
    @Transactional
    public Void delete(@RequestParam String UserGroupId, JwtAuthenticationToken token) {
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow(() -> new UserNotFoundException());
        
        if(user.hasRole("API_GROUP_MANAGER") || user.isAdmin()){
            var userGroup = userGroupRep.findById(UUID.fromString(UserGroupId)).orElseThrow( () -> new ObjectNotFoundException());
            userGroupRep.delete(userGroup);
            
        } else {
            throw new UserNotAuthorizedException();
        }

        return null;
    }
    
    @Override
    @Transactional
    public UserGroup addUsertToGroup(@RequestParam String userId, @RequestParam String groupId, JwtAuthenticationToken token) {
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow(() -> new UserNotFoundException());
        
        if(!user.hasRole("API_GROUP_MANAGER")){
            throw new UserNotAuthorizedException();
        }

        var userToAdd = userRep.findById(UUID.fromString(userId)).orElseThrow(() -> new UserNotFoundException());
        
        if(userToAdd.hasGroup(UUID.fromString(groupId))){
            throw new ObjectNotProcessableException();
        }

        var group = groupRep.findById(UUID.fromString(groupId)).orElseThrow( () -> new ObjectNotFoundException());

        try {
            var userGroup = new UserGroup();
            userGroup.setUser(user);
            userGroup.setGroup(group);
            userGroupRep.save(userGroup);
            return userGroup;
        } catch (Exception e) {
            throw new ObjectNotProcessableException();
        }

    }

    @Override
    @Transactional
    public Void removeUserFromGroup(@RequestParam String userId, @RequestParam String groupId, JwtAuthenticationToken token) {
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow(() -> new UserNotFoundException());
        
        if(!user.hasRole("API_GROUP_MANAGER")){
            throw new UserNotAuthorizedException();
        }

        return null;
    }

    @Override
    @Transactional
    public List<User> findUsersByGroupId(@RequestParam String groupId, @RequestParam JwtAuthenticationToken token) {
        
        var group = groupRep.findById(UUID.fromString(groupId)).orElseThrow( () -> new ObjectNotFoundException());
        var users = userGroupRep.findUsersByGroup(group);
        return users;
    }

    @Override
    @Transactional
    public List<Group> findGroupsByUserId(@RequestParam String userId, JwtAuthenticationToken token) {
        
        var user = userRep.findById(UUID.fromString(userId)).orElseThrow( () -> new UserNotFoundException());
        var groups = userGroupRep.findGroupsByUser(user);
        return groups;
    }
}
