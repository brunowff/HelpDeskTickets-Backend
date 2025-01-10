/**
 * A Data Transfer Object (DTO) representing a paginated item of a ticket log.
 * This record encapsulates the details of a ticket log entry.
 *
 * @param ticketLogId       the unique identifier of the ticket log
 * @param ticketId          the ticket associated with the log entry
 * @param user              the user who created the log entry
 * @param description       the description of the log entry
 * @param creationDateTime  the date and time when the log entry was created
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.Date;
import java.util.UUID;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketLog;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;

public record PageItemTicketLogDto(
    UUID ticketLogId,
    Ticket ticketId,
    User user,
    String description,
    Date creationDateTime
) {
    public PageItemTicketLogDto(TicketLog ticketLog) {
        this(ticketLog.getTicketLogId(), ticketLog.getTicket(), ticketLog.getUser(), ticketLog.getLogDescription(), ticketLog.getLogDateTime());
    }
}
