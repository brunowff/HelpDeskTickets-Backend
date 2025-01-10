/**
 * Data Transfer Object for Ticket Log.
 * 
 * @param logDescription Description of the log entry.
 * @param userId ID of the user who created the log entry.
 * @param ticketId ID of the ticket associated with the log entry.
 * @param logDateTime Date and time when the log entry was created.
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.Date;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record TicketLogDto(
    @NotBlank(message = "{required.validation}")
    String logDescription,

    @NotBlank(message = "{required.validation}")    
    UUID userId,

    @NotBlank(message = "{required.validation}")
    UUID ticketId,

    @NotBlank(message = "{required.validation}")
    Date logDateTime
) {

}
