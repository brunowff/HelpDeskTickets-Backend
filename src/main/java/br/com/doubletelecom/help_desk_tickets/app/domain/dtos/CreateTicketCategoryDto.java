/**
 * Data Transfer Object for creating a new ticket category.
 * 
 * @param name the name of the ticket category, must not be blank.
 * @param destinationGroup the group to which the ticket category is assigned, must not be blank.
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTicketCategoryDto(
    
    @NotBlank(message = "{required.validation}")
    String name,
    
    @NotNull(message = "{required.validation}")
    Group destinationGroup
) {

}
