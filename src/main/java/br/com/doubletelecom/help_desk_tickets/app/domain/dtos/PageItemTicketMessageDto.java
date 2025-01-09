package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.Date;
import java.util.UUID;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketMessage;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;

public record PageItemTicketMessageDto(
    UUID ticketMessageId,
    Ticket ticketId,
    User user,
    String message,
    Date creationDateTime
) {
    public PageItemTicketMessageDto(TicketMessage ticketMessage) {
        this(ticketMessage.getTicketMessageId(), ticketMessage.getTicket(), ticketMessage.getUser(), ticketMessage.getMessage(), ticketMessage.getMessageDateTime());
    }
}
