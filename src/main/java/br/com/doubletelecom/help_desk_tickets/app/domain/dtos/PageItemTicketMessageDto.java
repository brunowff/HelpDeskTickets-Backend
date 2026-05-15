/**
 * A Data Transfer Object (DTO) representing a page item for a ticket message.
 * This DTO is used to transfer data related to a ticket message between different layers of the application.
 *
 * @param ticketMessageId The unique identifier of the ticket message.
 * @param ticketId The ticket associated with the message.
 * @param user The user who created the message.
 * @param message The content of the message.
 * @param creationDateTime The date and time when the message was created.
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.time.Instant;
import java.util.UUID;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketMessage;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;

public record PageItemTicketMessageDto(
    UUID ticketMessageId,
    Ticket ticketId,
    User user,
    String message,
    Instant creationDateTime
) {
    public PageItemTicketMessageDto(TicketMessage ticketMessage) {
        this(ticketMessage.getTicketMessageId(), ticketMessage.getTicket(), ticketMessage.getUser(), ticketMessage.getMessage(), ticketMessage.getMessageDateTime());
    }
}
