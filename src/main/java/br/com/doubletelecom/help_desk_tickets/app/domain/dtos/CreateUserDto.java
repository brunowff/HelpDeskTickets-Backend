package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDto(
    @NotBlank(message = "{required.validation}")
    @Size(min = 7, max = 30, message = "{size.validation}")
    String username, 
    
    @NotBlank(message = "{required.validation}")
    @Size(min = 6, max = 30, message = "{size.validation}")
    String password, 
    
    @NotBlank(message = "{required.validation}")
    String fullname) {

}
