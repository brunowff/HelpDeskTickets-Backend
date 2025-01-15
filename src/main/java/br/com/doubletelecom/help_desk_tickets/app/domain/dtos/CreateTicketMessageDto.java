/**
 * Data Transfer Object for creating a ticket message.
 * 
 * @param message The content of the ticket message. Must not be blank.
 * @param ticketId The unique identifier of the ticket. Must not be blank.
 * @param userId The unique identifier of the user creating the message. Must not be blank.
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTicketMessageDto(
    @NotBlank(message = "{required.validation}")
    String message,

    @NotNull(message = "{required.validation}")
    UUID ticketId,

    @NotNull(message = "{required.validation}")
    UUID userId
) {

}
