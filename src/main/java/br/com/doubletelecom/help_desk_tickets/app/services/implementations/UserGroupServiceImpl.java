package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.List;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateUserGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.UserGroup;
import br.com.doubletelecom.help_desk_tickets.app.services.UserGroupServices;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

public class UserGroupServiceImpl implements UserGroupServices{
    
    @Override
    @Transactional
    public UserGroup save(@RequestBody @Valid CreateUserGroupDto UserGroupDto, JwtAuthenticationToken token) {
        // TODO
        return null;
    }

    @Override
    @Transactional
    public UserGroup findById(@RequestParam String UserGroupId, JwtAuthenticationToken token) {
        // TODO
        return null;
    }

    @Override
    @Transactional
    public List<UserGroup> findAll() {
        // TODO
        return null;
    }

    @Override
    @Transactional
    public Void delete(@RequestParam String UserGroupId, JwtAuthenticationToken token) {
        // TODO
        return null;
    }
    
    @Override
    @Transactional
    public UserGroup addUsertToGroup(@RequestParam String userId, @RequestParam String groupId, JwtAuthenticationToken token) {
        // TODO
        return null;
    }

    @Override
    @Transactional
    public Void removeUserFromGroup(@RequestParam String userId, @RequestParam String groupId, JwtAuthenticationToken token) {
        // TODO
        return null;
    }

    @Override
    @Transactional
    public List<User> findUsersByGroupId(@RequestParam String groupId, @RequestParam JwtAuthenticationToken token) {
        // TODO
        return null;
    }

    @Override
    @Transactional
    public List<Group> findGroupsByUserId(@RequestParam String userId, JwtAuthenticationToken token) {
        // TODO
        return null;
    }
}
