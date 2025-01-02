
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
    Void activate(String groupId, JwtAuthenticationToken token);
    Void deactivate(String groupId, JwtAuthenticationToken token);
}
