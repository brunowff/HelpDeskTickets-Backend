package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.List;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateUserGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.UserGroup;
import br.com.doubletelecom.help_desk_tickets.app.services.UserGroupServices;
import jakarta.transaction.Transactional;

public class UserGroupServiceImpl implements UserGroupServices{
    
    @Override
    @Transactional
    public UserGroup save(CreateUserGroupDto UserGroupDto, JwtAuthenticationToken token) {
        // TODO
        return null;
    }

    @Override
    @Transactional
    public UserGroup findById(String UserGroupId, JwtAuthenticationToken token) {
        // TODO
        return null;
    }

    @Override
    @Transactional
    public List<UserGroup> findAll(JwtAuthenticationToken token) {
        // TODO
        return null;
    }

    @Override
    @Transactional
    public Void delete(String UserGroupId, JwtAuthenticationToken token) {
        // TODO
        return null;
    }
    
    @Override
    @Transactional
    public UserGroup addUsertToGroup(String userId, String groupId, JwtAuthenticationToken token) {
        // TODO
        return null;
    }

    @Override
    @Transactional
    public Void removeUserFromGroup(String userId, String groupId, JwtAuthenticationToken token) {
        // TODO
        return null;
    }

    @Override
    @Transactional
    public List<User> findUsersByGroupId(String groupId, JwtAuthenticationToken token) {
        // TODO
        return null;
    }

    @Override
    @Transactional
    public List<Group> findGroupsByUserId(String userId, JwtAuthenticationToken token) {
        // TODO
        return null;
    }
}
