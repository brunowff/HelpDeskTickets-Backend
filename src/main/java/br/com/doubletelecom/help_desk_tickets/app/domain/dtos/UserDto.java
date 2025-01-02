package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDto(

    @NotBlank(message = "{required.validation}")
    UUID userId,

    @NotBlank(message = "{required.validation}")
    String fullname,

    @NotBlank(message = "{required.validation}")
    @Size(min = 7, max = 30, message = "{size.validation}")
    String username,

    @NotBlank(message = "{required.validation}")
    @Email(message = "{email.validation}")
    String email, 
    
    @NotBlank(message = "{required.validation}")
    @Size(min = 6, max = 30, message = "{size.validation}")
    String password,

    @NotBlank(message = "{required.validation}")
    Boolean active
    
    ) {
}
