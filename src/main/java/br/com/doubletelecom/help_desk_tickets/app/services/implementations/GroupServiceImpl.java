package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.GroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;
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
    public Group save(@RequestBody CreateGroupDto groupDto, JwtAuthenticationToken token) {
        
        var group = new Group();
        
        try {
            groupRep.save(group);
            return group;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    @Transactional
    public Group findById(@RequestBody String groupId, JwtAuthenticationToken token) {
        var group = groupRep.findById(UUID.fromString(groupId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return group;
    }
    
    @Override
    @Transactional
    public List<Group> findAll() {
        var groups = groupRep.findAll();
        return groups;
    }

    @Override
    @Transactional
    public Group update(@RequestBody GroupDto groupDto, JwtAuthenticationToken token) {
        var group = groupRep.findById(groupDto.groupId()).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        try {
            groupRep.save(group);
            return group;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @Override
    @Transactional
    public Void delete(@RequestBody String groupId, JwtAuthenticationToken token) {
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var group = groupRep.findById(UUID.fromString(groupId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(user.isAdmin()){
            groupRep.delete(group);
            return null;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

}
