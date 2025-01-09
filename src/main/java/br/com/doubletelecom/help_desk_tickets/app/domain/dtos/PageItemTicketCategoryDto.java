package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.UUID;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketCategory;

public record PageItemTicketCategoryDto(
    UUID ticketCategoryId,
    String name,
    Group destinationGroup,
    Boolean active
) {
    public PageItemTicketCategoryDto(TicketCategory ticketCategory) {
        this(ticketCategory.getTicketCategoryId(), ticketCategory.getName(), ticketCategory.getDestinationGroup(), ticketCategory.getActive());
    }
}
