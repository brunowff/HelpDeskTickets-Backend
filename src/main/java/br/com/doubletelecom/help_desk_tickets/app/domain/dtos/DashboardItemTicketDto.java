package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.Date;
import java.util.UUID;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketType;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;

public record DashboardItemTicketDto(
    UUID PostId, 
    String title,
    String description,
    TicketType ticketType,
    String status,
    String priority, 
    User userAuthor,
    User attribuitedTo,
    Date creationDateTime,
    Date finalizationDateTime
    ) {

}
