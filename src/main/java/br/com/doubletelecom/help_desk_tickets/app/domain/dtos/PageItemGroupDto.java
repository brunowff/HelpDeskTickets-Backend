/**
 * Data Transfer Object (DTO) for representing a group item in a paginated list.
 * This DTO is used to transfer data between different layers of the application.
 *
 * @param groupId the unique identifier of the group
 * @param name the name of the group
 * @param active the status indicating whether the group is active
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.UUID;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;

public record PageItemGroupDto(
    UUID groupId,
    String name,
    String description,
    Boolean active
) {
    public PageItemGroupDto(Group group) {
        this(group.getGroupId(), group.getName(), group.getDescription(), group.getActive());
    }
}
