/**
 * Data Transfer Object for Ticket Category.
 * 
 * @param ticketCategoryId Unique identifier for the ticket category.
 * @param name Name of the ticket category.
 * @param destinationGroup Group to which the ticket category is assigned.
 * @param active Status indicating whether the ticket category is active.
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.UUID;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;
import jakarta.validation.constraints.NotBlank;

public record TicketCategoryDto(
    @NotBlank(message = "{required.validation}")
    UUID ticketCategoryId,

    @NotBlank(message = "{required.validation}")
    String name,
    
    @NotBlank(message = "{required.validation}")
    Group destinationGroup,

    @NotBlank(message = "{required.validation}")
    Boolean active
) {

}
