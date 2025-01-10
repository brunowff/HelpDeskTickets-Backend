/**
 * Data Transfer Object for Ticket.
 * 
 * @param ticketId Unique identifier for the ticket.
 * @param ticketTitle Title of the ticket.
 * @param ticketDescription Description of the ticket.
 * @param ticketStatus Status of the ticket.
 * @param ticketPriority Priority level of the ticket.
 * @param ticketCategory Category identifier for the ticket.
 * @param userId Identifier of the user who created the ticket.
 * @param attibuitedToUserId Identifier of the user to whom the ticket is assigned.
 * @param creationDateTime Date and time when the ticket was created.
 * @param finalizationDateTime Date and time when the ticket was finalized.
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.Date;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record TicketDto(

    @NotBlank(message = "{required.validation}")
    UUID ticketId,

    @NotBlank(message = "{required.validation}")
    String ticketTitle,

    @NotBlank(message = "{required.validation}")
    String ticketDescription,

    @NotBlank(message = "{required.validation}")
    String ticketStatus,
    
    @NotBlank(message = "{required.validation}")
    String ticketPriority,

    @NotBlank(message = "{required.validation}")
    UUID ticketCategory,

    @NotBlank(message = "{required.validation}")
    UUID userId,

    @NotBlank(message = "{required.validation}")
    UUID attibuitedToUserId,

    @NotBlank(message = "{required.validation}")
    Date creationDateTime,

    @NotBlank(message = "{required.validation}")
    Date finalizationDateTime
    
    ) {

}
