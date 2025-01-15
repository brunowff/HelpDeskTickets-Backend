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
import jakarta.validation.constraints.NotNull;

public record TicketLogDto(
    @NotBlank(message = "{required.validation}")
    String logDescription,

    @NotNull(message = "{required.validation}")    
    UUID userId,

    @NotNull(message = "{required.validation}")
    UUID ticketId,

    @NotNull(message = "{required.validation}")
    Date logDateTime
) {

}
