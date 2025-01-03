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
