package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreateTicketMessageDto(
    @NotBlank(message = "{required.validation}")
    String message,

    @NotBlank(message = "{required.validation}")
    String ticketId,

    @NotBlank(message = "{required.validation}")
    String userId
) {

}
