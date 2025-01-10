/**
 * Data Transfer Object for creating a new ticket.
 * 
 * @param ticketTitle       the title of the ticket, must not be blank
 * @param ticketDescription the description of the ticket, must not be blank
 * @param ticketPriority    the priority of the ticket, must not be blank
 * @param ticketCategory    the category of the ticket, must not be blank
 * @param userId            the ID of the user creating the ticket, must not be blank
 * @param attibuitedToUserId the ID of the user to whom the ticket is attributed, must not be blank
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record CreateTicketDto(

    @NotBlank(message = "{required.validation}")
    String ticketTitle,

    @NotBlank(message = "{required.validation}")
    String ticketDescription,

    @NotBlank(message = "{required.validation}")
    String ticketPriority,

    @NotBlank(message = "{required.validation}")
    UUID ticketCategory,

    @NotBlank(message = "{required.validation}")
    UUID userId,

    @NotBlank(message = "{required.validation}")
    UUID attibuitedToUserId
) {

}
