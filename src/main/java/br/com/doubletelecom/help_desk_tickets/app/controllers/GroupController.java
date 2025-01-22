
/**
 * GroupController is a REST controller that manages group-related operations.
 * It provides endpoints for creating, updating, retrieving, activating, deactivating,
 * and managing users within groups.
 * 
 * <p>Endpoints:</p>
 * <ul>
 *   <li>POST /group-manager/groups - Create a new group</li>
 *   <li>PUT /group-manager/groups/{id} - Update an existing group</li>
 *   <li>GET /group-manager/groups - Retrieve all groups with pagination</li>
 *   <li>GET /group-manager/groups/{id} - Retrieve a group by its ID</li>
 *   <li>PATCH /group-manager/groups/{id}/activate - Activate a group</li>
 *   <li>PATCH /group-manager/groups/{id}/deactivate - Deactivate a group</li>
 *   <li>POST /group-manager/groups/{id}/add-user/{userId} - Add a user to a group</li>
 *   <li>DELETE /group-manager/groups/{id}/remove-user/{userId} - Remove a user from a group</li>
 *   <li>GET /group-manager/groups/{id}/find-by-user - Find groups by user ID</li>
 * </ul>
 * 
 * <p>Authorization:</p>
 * <ul>
 *   <li>Requires 'SCOPE_API_BASIC' or 'SCOPE_API_GROUP' for general access</li>
 *   <li>Requires 'SCOPE_API_ADMIN' or 'SCOPE_API_GROUP_MANAGER' for group management operations</li>
 * </ul>
 * 
 * <p>Dependencies:</p>
 * <ul>
 *   <li>UserServices - Service for user-related operations</li>
 *   <li>GroupServices - Service for group-related operations</li>
 * </ul>
 * 
 * <p>Exceptions:</p>
 * <ul>
 *   <li>ObjectNotFoundException - Thrown when a requested object is not found</li>
 * </ul>
 * 
 * <p>Annotations:</p>
 * <ul>
 *   <li>@RestController - Indicates that this class is a REST controller</li>
 *   <li>@RequestMapping("/group-manager") - Base URL for all endpoints in this controller</li>
 *   <li>@AllArgsConstructor - Generates a constructor with 1 parameter for each field in this class</li>
 *   <li>@PreAuthorize - Specifies security constraints on methods</li>
 * </ul>
 */
package br.com.doubletelecom.help_desk_tickets.app.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.GroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.ObjectNotFoundException;
import br.com.doubletelecom.help_desk_tickets.app.services.GroupServices;
import br.com.doubletelecom.help_desk_tickets.app.services.UserServices;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@AllArgsConstructor
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/group-manager")
@PreAuthorize("hasAuthority('SCOPE_API_BASIC') or hasAuthority('SCOPE_API_GROUP')")
public class GroupController {

    private final UserServices userServices;
    private final GroupServices groupServices;

    @PostMapping("/groups")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_GROUP_MANAGER')")
    public ResponseEntity<PageItemGroupDto> create(@RequestBody @Validated CreateGroupDto createGroupDto, JwtAuthenticationToken token, UriComponentsBuilder uriBuilder) {
        var group = groupServices.save(createGroupDto, token);
        var uri = uriBuilder.path("/groups/{id}").buildAndExpand(group.getGroupId()).toUri();
        return ResponseEntity.created(uri).body(new PageItemGroupDto(group));
    }

    @PutMapping("/groups/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_GROUP_MANAGER')")
    public ResponseEntity<Void> update(@RequestBody @Validated GroupDto groupDto, JwtAuthenticationToken token) {
        groupServices.update(groupDto, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/groups")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_GROUP_MANAGER')")
    public ResponseEntity<Page<PageItemGroupDto>> findAll(Pageable pageable, JwtAuthenticationToken token) {
        var groups = groupServices.findAll(pageable);
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/groups/{id}")
    public ResponseEntity<Group> findById(@PathVariable("id") String groupId, JwtAuthenticationToken token) {
        var group = groupServices.findById(groupId, token);
        return ResponseEntity.ok(group);
    }
    
    @PatchMapping("/groups/{id}/activate")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_GROUP_MANAGER')")
    public ResponseEntity<Void> activate(@PathVariable("id") String groupId, JwtAuthenticationToken token) {
        groupServices.activate(groupId, token);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/groups/{id}/deactivate")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_GROUP_MANAGER')")
    public ResponseEntity<Void> deactivate(@PathVariable("id") String groupId, JwtAuthenticationToken token) {
        groupServices.deactivate(groupId, token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/groups/{id}/add-user/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_GROUP_MANAGER')")
    public ResponseEntity<Void> addUserToGroup(@PathVariable("id") String groupId, @PathVariable("userId") String userId, JwtAuthenticationToken token) {
        userServices.addUserToGroup(userId, groupId, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/groups/{id}/remove-user/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_GROUP_MANAGER')")
    public ResponseEntity<Void> removeUserFromGroup(@PathVariable("id") String groupId, @PathVariable("userId") String userId, JwtAuthenticationToken token) {
        userServices.removeUserFromGroup(userId, groupId, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/groups/{id}/find-by-user")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_GROUP_MANAGER') or hasAuthority('SCOPE_API_GROUP')")
    public ResponseEntity<List<PageItemGroupDto>> findGroupsByUser(@PathVariable("id") String userId, JwtAuthenticationToken token) {
        try {
            var groups = userServices.findGroupsByUserId(userId, token);
            return ResponseEntity.ok(groups);
        } catch (Exception e) {
            throw new ObjectNotFoundException();
        }
    }


}
