package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateTicketTypeDto(
    
    @NotBlank(message = "{required.validation}")
    String name
    
) {

}
