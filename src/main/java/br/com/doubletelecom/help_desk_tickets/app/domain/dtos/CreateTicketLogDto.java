/**
 * Data Transfer Object for creating a ticket log.
 * 
 * @param logDescription Description of the log entry.
 * @param userId ID of the user creating the log.
 * @param ticketId ID of the ticket associated with the log.
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTicketLogDto(
    
    @NotBlank(message = "{required.validation}")
    String logDescription,

    @NotNull(message = "{required.validation}")    
    UUID userId,

    @NotNull(message = "{required.validation}")
    UUID ticketId

) {

}
