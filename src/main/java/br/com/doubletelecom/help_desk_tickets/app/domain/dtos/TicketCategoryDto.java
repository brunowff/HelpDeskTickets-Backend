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
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketCategoryDto(
    @NotNull(message = "{required.validation}")
    UUID ticketCategoryId,

    @NotBlank(message = "{required.validation}")
    String name,
    
    @NotNull(message = "{required.validation}")
    Group destinationGroup,

    @NotNull(message = "{required.validation}")
    Boolean active
) {
    public TicketCategoryDto(TicketCategory ticketCategory) {
        this(ticketCategory.getTicketCategoryId(), ticketCategory.getName(), ticketCategory.getDestinationGroup(), ticketCategory.getActive());
    }
}
