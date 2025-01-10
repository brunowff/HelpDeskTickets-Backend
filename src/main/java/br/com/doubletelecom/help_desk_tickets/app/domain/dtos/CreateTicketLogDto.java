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

public record CreateTicketLogDto(
    
    @NotBlank(message = "{required.validation}")
    String logDescription,

    @NotBlank(message = "{required.validation}")    
    UUID userId,

    @NotBlank(message = "{required.validation}")
    UUID ticketId

) {

}
