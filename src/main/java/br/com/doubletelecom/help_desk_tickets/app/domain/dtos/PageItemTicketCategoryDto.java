/**
 * A Data Transfer Object (DTO) representing a page item of a ticket category.
 * This DTO is used to transfer data related to ticket categories in a paginated format.
 *
 * @param ticketCategoryId the unique identifier of the ticket category
 * @param name the name of the ticket category
 * @param destinationGroup the group to which the ticket category is assigned
 * @param active the status indicating whether the ticket category is active
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.UUID;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketCategory;

public record PageItemTicketCategoryDto(
    UUID ticketCategoryId,
    String name,
    Group destinationGroup,
    Boolean active
) {
    public PageItemTicketCategoryDto(TicketCategory ticketCategory) {
        this(ticketCategory.getTicketCategoryId(), ticketCategory.getName(), ticketCategory.getDestinationGroup(), ticketCategory.getActive());
    }
}
