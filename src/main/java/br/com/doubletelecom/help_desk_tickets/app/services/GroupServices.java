package br.com.doubletelecom.help_desk_tickets.app.services;

import java.util.List;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;

public interface GroupServices {
    Group save(CreateGroupDto groupDto, JwtAuthenticationToken token);
    Group findById(String groupId);
    List<Group> findAll();
    Group update(CreateGroupDto groupDto, JwtAuthenticationToken token);
    Void delete(String groupId, JwtAuthenticationToken token);

}
