package br.com.doubletelecom.help_desk_tickets.app.services;

import java.util.List;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.GroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;

public interface GroupServices {
    Group save(CreateGroupDto groupDto, JwtAuthenticationToken token);
    Group findById(String groupId, JwtAuthenticationToken token);
    List<Group> findAll();
    Group update(GroupDto groupDto, JwtAuthenticationToken token);
    Void delete(String groupId, JwtAuthenticationToken token);

}
