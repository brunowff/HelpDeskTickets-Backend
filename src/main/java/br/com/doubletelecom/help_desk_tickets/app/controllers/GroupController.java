package br.com.doubletelecom.help_desk_tickets.app.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.RestController;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.GroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemGroupDto;
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

    @PostMapping("/groups")
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
    public ResponseEntity<Page<PageItemGroupDto>> findAll(Pageable pageable, JwtAuthenticationToken token) {
        var groups = groupServices.findAll(pageable);
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/group/{id}")
    public ResponseEntity<Group> findById(@PathVariable("id") String groupId ,JwtAuthenticationToken token) {
        var group = groupServices.findById(groupId, token);
        return ResponseEntity.ok(group);
    }

    @GetMapping("/group/activate/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_GROUP_MANAGER')")
    public ResponseEntity<Void> activate(@PathVariable("id") String groupId,JwtAuthenticationToken token) {
        groupServices.activate(groupId, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/group/deactivate/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_GROUP_MANAGER')")
    public ResponseEntity<Void> deactivate(@PathVariable("id") String groupId,JwtAuthenticationToken token) {
        groupServices.deactivate(groupId, token);
        return ResponseEntity.ok().build();
    }

}
