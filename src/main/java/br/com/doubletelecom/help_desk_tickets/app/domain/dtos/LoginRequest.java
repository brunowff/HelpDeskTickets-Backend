package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "{required.validation}") String email, 
    @NotBlank(message = "{required.validation}") String password) {
        
}
