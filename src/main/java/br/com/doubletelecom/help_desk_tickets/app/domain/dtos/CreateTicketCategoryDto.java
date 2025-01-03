package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;
import jakarta.validation.constraints.NotBlank;

public record CreateTicketCategoryDto(
    
    @NotBlank(message = "{required.validation}")
    String name,
    
    @NotBlank(message = "{required.validation}")
    Group destinationGroup
) {

}
