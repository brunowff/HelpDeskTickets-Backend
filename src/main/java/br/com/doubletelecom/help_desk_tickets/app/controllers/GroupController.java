/**
 * GroupController is a REST controller that handles HTTP requests for managing groups.
 * It provides endpoints for creating, updating, deleting, retrieving, activating, and deactivating groups.
 * 
 * Endpoints:
 * - POST /groups: Create a new group. Requires SCOPE_API_ADMIN or SCOPE_API_GROUP_MANAGER authority.
 * - PUT /group/{id}: Update an existing group. Requires SCOPE_API_ADMIN or SCOPE_API_GROUP_MANAGER authority.
 * - DELETE /group/{id}: Delete a group by ID. Requires SCOPE_API_ADMIN or SCOPE_API_GROUP_MANAGER authority.
 * - GET /groups: Retrieve a paginated list of groups. Requires SCOPE_API_BASIC or SCOPE_API_GROUP authority.
 * - GET /group/{id}: Retrieve a group by ID. Requires SCOPE_API_BASIC or SCOPE_API_GROUP authority.
 * - GET /group/activate/{id}: Activate a group by ID. Requires SCOPE_API_ADMIN or SCOPE_API_GROUP_MANAGER authority.
 * - GET /group/deactivate/{id}: Deactivate a group by ID. Requires SCOPE_API_ADMIN or SCOPE_API_GROUP_MANAGER authority.
 * 
 * Security:
 * - The controller is secured with OAuth2 and requires specific authorities for certain operations.
 * - General access requires either SCOPE_API_BASIC or SCOPE_API_GROUP authority.
 * 
 * Dependencies:
 * - GroupServices: Service layer for handling group-related operations.
 * 
 * Annotations:
 * - @RestController: Marks the class as a REST controller.
 * - @AllArgsConstructor: Generates a constructor with one parameter for each field in the class.
 * - @PreAuthorize: Specifies security constraints on methods.
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.controllers;

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
@RequestMapping("/group-manager")
@AllArgsConstructor
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

    @GetMapping("/groups/{id}/find-by-user")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_GROUP_MANAGER') or hasAuthority('SCOPE_API_GROUP')")
    public ResponseEntity<Page<PageItemGroupDto>> findGroupsByUser(@PathVariable("id") String userId, JwtAuthenticationToken token, Pageable pageable) {
        try {
            var groups = userServices.findGroupsByUser(userId, token, pageable);
            return ResponseEntity.ok(groups);

        } catch (Exception e) {
            throw new ObjectNotFoundException();
        }
    }
    
    @PatchMapping("/groups/activate/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_GROUP_MANAGER')")
    public ResponseEntity<Void> activate(@PathVariable("id") String groupId, JwtAuthenticationToken token) {
        groupServices.activate(groupId, token);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/groups/deactivate/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_GROUP_MANAGER')")
    public ResponseEntity<Void> deactivate(@PathVariable("id") String groupId, JwtAuthenticationToken token) {
        groupServices.deactivate(groupId, token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/groups/{id}/add-user/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_GROUP_MANAGER')")
    public ResponseEntity<Void> addUserToGroup(@PathVariable("id") String groupId, @PathVariable("userId") String userId, JwtAuthenticationToken token) {
        userServices.addGroupToUser(userId, groupId, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/groups/{id}/remove-user/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_GROUP_MANAGER')")
    public ResponseEntity<Void> removeUserFromGroup(@PathVariable("id") String groupId, @PathVariable("userId") String userId, JwtAuthenticationToken token) {
        userServices.removeGroupFromUser(userId, groupId, token);
        return ResponseEntity.ok().build();
    }


}
