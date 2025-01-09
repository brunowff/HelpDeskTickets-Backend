package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.UUID;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;

public record PageItemGroupDto(
    UUID groupId,
    String name,
    Boolean active
) {
    public PageItemGroupDto(Group group) {
        this(group.getGroupId(), group.getName(), group.getActive());
    }
}
