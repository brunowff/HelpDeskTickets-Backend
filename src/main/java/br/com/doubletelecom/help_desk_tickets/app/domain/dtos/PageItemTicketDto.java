package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.Date;
import java.util.UUID;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketCategory;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;

public record PageItemTicketDto(
    UUID PostId, 
    String title,
    String description,
    TicketCategory ticketCategory,
    String status,
    String priority, 
    User userAuthor,
    User attribuitedTo,
    Date creationDateTime,
    Date finalizationDateTime
    ) {
        public PageItemTicketDto(Ticket ticket){
            this(ticket.getTicketId(), ticket.getTicketTitle(), ticket.getTicketDescription(), ticket.getTicketCategory(), ticket.getTicketStatus(), ticket.getTicketPriority(), ticket.getUser(), ticket.getAttribuitedToUser(), Date.from(ticket.getCreationDateTime()), ticket.getFinalizationDateTime());
        }
}
