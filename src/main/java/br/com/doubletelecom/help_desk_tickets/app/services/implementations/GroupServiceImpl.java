package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.GroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;
import br.com.doubletelecom.help_desk_tickets.app.repositories.GroupRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.UserGroupRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.UserRepository;
import br.com.doubletelecom.help_desk_tickets.app.services.GroupServices;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class GroupServiceImpl implements GroupServices{

    private final UserRepository userRep;
    private final GroupRepository groupRep;
    private final UserGroupRepository userGroupRep;

    
    @Override
    @Transactional
    public Group save(@RequestBody @Valid CreateGroupDto groupDto, JwtAuthenticationToken token) {
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        
        if(user.hasRole("API_GROUP_MANAGER") || user.isAdmin()){
            try {
                var group = new Group();
                group.setName(groupDto.name());
                group.setDescription(groupDto.description());
                group.setActive(true);
                groupRep.save(group);
                return group;
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        
    }

    @Override
    @Transactional
    public Group findById(@RequestParam String groupId, JwtAuthenticationToken token) {
        var group = groupRep.findById(UUID.fromString(groupId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return group;
    }
    
    @Override
    @Transactional
    public Page<PageItemGroupDto> findAll(Pageable pageable) {
        return groupRep.findAll(pageable).map(PageItemGroupDto::new);
    }

    @Override
    @Transactional
    public Group update(@RequestBody @Valid GroupDto groupDto, JwtAuthenticationToken token) {
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var group = groupRep.findById(groupDto.groupId()).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        if(user.isAdmin() || user.hasRole("API_GROUP_MANAGER")){
            try {
                group.setName(groupDto.name());
                group.setDescription(groupDto.description());
                group.setActive(groupDto.active());
                groupRep.save(group);
                return group;
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    @Transactional
    public Void delete(@RequestParam String groupId, JwtAuthenticationToken token) {
        
        // It is not recommended to delete a group, only deactivate it to avoid losing the ticket history

        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var group = groupRep.findById(UUID.fromString(groupId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        var userGroups = userGroupRep.findByGroup(group);
        if (userGroups != null && !userGroups.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Group has associated users and cannot be deleted");
        }

        if(user.isAdmin()){
            groupRep.delete(group);
            return null;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    @Transactional
    public Void activate(@RequestParam String groupId, JwtAuthenticationToken token) {
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(user.isAdmin() || user.hasRole("API_GROUP_MANAGER")){
            var group = groupRep.findById(UUID.fromString(groupId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            group.setActive(true);
            groupRep.save(group);
            return null;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

    }

    @Override
    @Transactional
    public Void deactivate(@RequestParam String groupId, JwtAuthenticationToken token) {
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(user.isAdmin() || user.hasRole("API_GROUP_MANAGER")){
            var group = groupRep.findById(UUID.fromString(groupId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            group.setActive(false);
            groupRep.save(group);
            return null;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

    }

}
