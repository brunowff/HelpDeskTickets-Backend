
/**
 * Service interface for managing Groups.
 * Provides methods for saving, finding, updating, deleting, activating, and deactivating groups.
 * 
 * Methods:
 * - save(CreateGroupDto groupDto, JwtAuthenticationToken token): Saves a new group.
 * - findById(String groupId, JwtAuthenticationToken token): Finds a group by its ID.
 * - findAll(): Retrieves a list of all groups.
 * - update(GroupDto groupDto, JwtAuthenticationToken token): Updates an existing group.
 * - delete(String groupId, JwtAuthenticationToken token): Deletes a group by its ID.
 * - activate(String groupId, JwtAuthenticationToken token): Activates a group by its ID.
 * - deactivate(String groupId, JwtAuthenticationToken token): Deactivates a group by its ID.
 */
package br.com.doubletelecom.help_desk_tickets.app.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.GroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemGroupDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;

public interface GroupServices {
    public Group save(CreateGroupDto groupDto, JwtAuthenticationToken token);
    public Group findById(String groupId, JwtAuthenticationToken token);
    public Page<PageItemGroupDto> findAll(Pageable pageable);
    public Group update(GroupDto groupDto, JwtAuthenticationToken token);
    public Void delete(String groupId, JwtAuthenticationToken token);
    public Void activate(String groupId, JwtAuthenticationToken token);
    public Void deactivate(String groupId, JwtAuthenticationToken token);
}
