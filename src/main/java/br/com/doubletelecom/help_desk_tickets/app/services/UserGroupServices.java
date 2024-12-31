package br.com.doubletelecom.help_desk_tickets.app.services;

import java.util.List;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateUserGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.UserGroup;

public interface UserGroupServices {
    
    public UserGroup save(CreateUserGroupDto UserGroupDto, JwtAuthenticationToken token);
    public UserGroup findById(String UserGroupId, JwtAuthenticationToken token);
    public Void delete(String UserGroupId, JwtAuthenticationToken token);
    public UserGroup addUsertToGroup(String userId, String groupId, JwtAuthenticationToken token);
    public Void removeUserFromGroup(String userId, String groupId, JwtAuthenticationToken token);
    public List<UserGroup> findAll();
    public List<User> findUsersByGroupId(String groupId, JwtAuthenticationToken token);
    public List<Group> findGroupsByUserId(String userId, JwtAuthenticationToken token);

}
