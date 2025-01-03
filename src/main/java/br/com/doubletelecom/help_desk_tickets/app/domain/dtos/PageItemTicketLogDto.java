package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.Date;
import java.util.UUID;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;

public record PageItemTicketLogDto(
    UUID ticketLogId,
    Ticket ticketId,
    User user,
    String description,
    Date creationDateTime
) {

}
