package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.List;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;
import br.com.doubletelecom.help_desk_tickets.app.services.GroupServices;
import jakarta.transaction.Transactional;

public class GroupServiceImpl implements GroupServices{
    
    @Override
    @Transactional
    public Group save(CreateGroupDto groupDto, JwtAuthenticationToken token) {
        return null;
    }

    public Group findById(String groupId) {
        return null;
    }
    
    public List<Group> findAll() {
        return null;
    }

    public Group update(CreateGroupDto groupDto, JwtAuthenticationToken token) {
        return null;
    }

    public Void delete(String groupId, JwtAuthenticationToken token) {
        return null;
    }

}
