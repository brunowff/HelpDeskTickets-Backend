/**
 * Service implementation for managing groups.
 * This class provides methods to create, update, find, delete, activate, and deactivate groups.
 * It uses repositories to interact with the database and performs authorization checks based on user roles.
 * 
 * Methods:
 * - save(CreateGroupDto groupDto, JwtAuthenticationToken token): Creates a new group.
 * - findById(String groupId, JwtAuthenticationToken token): Finds a group by its ID.
 * - findAll(Pageable pageable): Retrieves all groups with pagination.
 * - update(GroupDto groupDto, JwtAuthenticationToken token): Updates an existing group.
 * - delete(String groupId, JwtAuthenticationToken token): Deletes a group (not recommended, prefer deactivation).
 * - activate(String groupId, JwtAuthenticationToken token): Activates a group.
 * - deactivate(String groupId, JwtAuthenticationToken token): Deactivates a group.
 * 
 * Exceptions:
 * - UserNotFoundException: Thrown when a user is not found.
 * - ObjectNotFoundException: Thrown when a group is not found.
 * - ObjectNotProcessableException: Thrown when an object cannot be processed.
 * - UserNotAuthorizedException: Thrown when a user is not authorized to perform an action.
 * 
 * Annotations:
 * - @Service: Indicates that this class is a service component in the Spring context.
 * - @AllArgsConstructor: Generates a constructor with one parameter for each field in the class.
 * - @Transactional: Indicates that the methods should be executed within a transaction context.
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.GroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.ObjectNotFoundException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.ObjectNotProcessableException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.UserNotAuthorizedException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.UserNotFoundException;
import br.com.doubletelecom.help_desk_tickets.app.repositories.GroupRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.UserRepository;
import br.com.doubletelecom.help_desk_tickets.app.services.GroupServices;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class GroupServiceImpl implements GroupServices{
    
    private final UserRepository userRep;
    private final GroupRepository groupRep;
    
    @Override
    @Transactional
    public Group save(@RequestBody @Validated CreateGroupDto groupDto, JwtAuthenticationToken token) {
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow(() -> new UserNotFoundException());
        
        if(user.hasRole("API_GROUP_MANAGER") || user.isAdmin()){
            try {
                var group = new Group();
                group.setName(groupDto.name());
                group.setDescription(groupDto.description());
                group.setActive(true);
                groupRep.save(group);
                return group;
            } catch (Exception e) {
                throw new ObjectNotProcessableException();
            }
        } else {
            throw new UserNotAuthorizedException();
        }

        
    }

    @Override
    @Transactional
    public Group findById(@RequestParam String groupId, JwtAuthenticationToken token) {
        var group = groupRep.findById(UUID.fromString(groupId)).orElseThrow( () -> new ObjectNotFoundException());
        return group;
    }
    
    @Override
    @Transactional
    public Page<PageItemGroupDto> findAll(Pageable pageable) {
        return groupRep.findAll(pageable).map(PageItemGroupDto::new);
    }

    @Override
    @Transactional
    public Group update(@RequestBody @Validated GroupDto groupDto, JwtAuthenticationToken token) {
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        var group = groupRep.findById(groupDto.groupId()).orElseThrow( () -> new ObjectNotFoundException());
        
        if(user.isAdmin() || user.hasRole("API_GROUP_MANAGER")){
            try {
                group.setName(groupDto.name());
                group.setDescription(groupDto.description());
                group.setActive(groupDto.active());
                groupRep.save(group);
                return group;
            } catch (Exception e) {
                throw new ObjectNotProcessableException();
            }
        } else {
            throw new UserNotAuthorizedException();
        }
    }


    @Override
    @Transactional
    public Void activate(@RequestParam String groupId, JwtAuthenticationToken token) {
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());

        if(user.isAdmin() || user.hasRole("API_GROUP_MANAGER")){
            var group = groupRep.findById(UUID.fromString(groupId)).orElseThrow( () -> new ObjectNotFoundException());
            group.setActive(true);
            groupRep.save(group);
            return null;
        } else {
            throw new UserNotAuthorizedException();
        }

    }

    @Override
    @Transactional
    public Void deactivate(@RequestParam String groupId, JwtAuthenticationToken token) {
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        
        if(user.isAdmin() || user.hasRole("API_GROUP_MANAGER")){
            var group = groupRep.findById(UUID.fromString(groupId)).orElseThrow( () -> new ObjectNotFoundException());
            group.setActive(false);
            groupRep.save(group);
            return null;
        } else {
            throw new UserNotAuthorizedException();
        }
        
    }

}
