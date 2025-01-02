package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateGroupDto(
    @NotBlank(message = "{required.validation}")
    @Size(min = 7, max = 30, message = "{size.validation}")
    String name,
    
    @Size(min = 7, max = 300, message = "{size.validation}")
    @NotBlank(message = "{required.validation}")
    String description
) {

}
