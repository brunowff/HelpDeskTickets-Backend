package br.com.doubletelecom.help_desk_tickets.app.controllers;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.GroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;
import br.com.doubletelecom.help_desk_tickets.app.services.GroupServices;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController("/gm")
@AllArgsConstructor
@PreAuthorize("hasAuthority('SCOPE_API_BASIC') or hasAuthority('SCOPE_API_GROUP')")
public class GroupController {

    private final GroupServices groupServices;

    @PostMapping("/group")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_GROUP_MANAGER')")
    public ResponseEntity<Void> create(@RequestBody @Valid CreateGroupDto createGroupDto, JwtAuthenticationToken token) {
        groupServices.save(createGroupDto, token);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/group/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_GROUP_MANAGER')")
    public ResponseEntity<Void> update(@RequestBody @Valid GroupDto GroupDto, JwtAuthenticationToken token) {
        groupServices.update(GroupDto, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/group/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_GROUP_MANAGER')")
    public ResponseEntity<Void> delete(@PathVariable("id") String groupId, JwtAuthenticationToken token) {
        groupServices.delete(groupId, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/groups")
    public ResponseEntity<List<Group>> findAll(JwtAuthenticationToken token) {
        var groups = groupServices.findAll();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/group/{id}")
    public ResponseEntity<Group> findById(@PathVariable("id") String groupId ,JwtAuthenticationToken token) {
        var group = groupServices.findById(groupId, token);
        return ResponseEntity.ok(group);
    }

    @GetMapping("/group/{id}/activate")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_GROUP_MANAGER')")
    public ResponseEntity<Void> activate(@PathVariable("id") String groupId,JwtAuthenticationToken token) {
        groupServices.activate(groupId, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/group/{id}/deactivate")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_GROUP_MANAGER')")
    public ResponseEntity<Void> deactivate(@PathVariable("id") String groupId,JwtAuthenticationToken token) {
        groupServices.deactivate(groupId, token);
        return ResponseEntity.ok().build();
    }

}
