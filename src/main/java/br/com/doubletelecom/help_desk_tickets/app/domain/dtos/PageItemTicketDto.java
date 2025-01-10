/**
 * A Data Transfer Object (DTO) representing a paginated item of a ticket.
 * This DTO is used to transfer ticket data between processes.
 *
 * @param PostId               the unique identifier of the ticket
 * @param title                the title of the ticket
 * @param description          the description of the ticket
 * @param ticketCategory       the category of the ticket
 * @param status               the current status of the ticket
 * @param priority             the priority level of the ticket
 * @param userAuthor           the user who created the ticket
 * @param attribuitedTo        the user to whom the ticket is assigned
 * @param creationDateTime     the date and time when the ticket was created
 * @param finalizationDateTime the date and time when the ticket was finalized
 * 
 * @author 
 * @version
 */
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
