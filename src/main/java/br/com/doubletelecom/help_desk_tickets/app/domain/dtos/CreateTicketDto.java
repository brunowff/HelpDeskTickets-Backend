package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateTicketDto(@NotBlank(message = "{required.validation}") String ticketTitle) {

}
