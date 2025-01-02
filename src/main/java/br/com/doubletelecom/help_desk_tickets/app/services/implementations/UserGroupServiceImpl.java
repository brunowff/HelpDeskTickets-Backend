package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateUserGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.UserGroup;
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

        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        var userToAdd = userRep.findById(userGroupDto.userId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        if(userToAdd.hasGroup(userGroupDto.groupId())){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User already in group");
        }

        if(user.isAdmin() || user.hasRole("API_GROUP_MANAGER")){
            var group = groupRep.findById(userGroupDto.groupId()).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

            try {
                var userGroup = new UserGroup();
                userGroup.setUser(user);
                userGroup.setGroup(group);
                userGroupRep.save(userGroup);
                return userGroup;
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    @Transactional
    public UserGroup findById(@RequestParam String UserGroupId, JwtAuthenticationToken token) {
        var userGroup = userGroupRep.findById(UUID.fromString(UserGroupId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
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
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        if(user.hasRole("API_GROUP_MANAGER") || user.isAdmin()){
            var userGroup = userGroupRep.findById(UUID.fromString(UserGroupId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            userGroupRep.delete(userGroup);
            
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return null;
    }
    
    @Override
    @Transactional
    public UserGroup addUsertToGroup(@RequestParam String userId, @RequestParam String groupId, JwtAuthenticationToken token) {
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        if(!user.hasRole("API_GROUP_MANAGER")){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        var userToAdd = userRep.findById(UUID.fromString(userId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        if(userToAdd.hasGroup(UUID.fromString(groupId))){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User already in group");
        }

        var group = groupRep.findById(UUID.fromString(groupId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        try {
            var userGroup = new UserGroup();
            userGroup.setUser(user);
            userGroup.setGroup(group);
            userGroupRep.save(userGroup);
            return userGroup;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @Override
    @Transactional
    public Void removeUserFromGroup(@RequestParam String userId, @RequestParam String groupId, JwtAuthenticationToken token) {
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        if(!user.hasRole("API_GROUP_MANAGER")){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return null;
    }

    @Override
    @Transactional
    public List<User> findUsersByGroupId(@RequestParam String groupId, @RequestParam JwtAuthenticationToken token) {
        
        var group = groupRep.findById(UUID.fromString(groupId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var users = userGroupRep.findUsersByGroup(group);
        return users;
    }

    @Override
    @Transactional
    public List<Group> findGroupsByUserId(@RequestParam String userId, JwtAuthenticationToken token) {
        
        var user = userRep.findById(UUID.fromString(userId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var groups = userGroupRep.findGroupsByUser(user);
        return groups;
    }
}
