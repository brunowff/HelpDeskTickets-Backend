package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record CreateTicketDto(

    @NotBlank(message = "{required.validation}")
    String ticketTitle,

    @NotBlank(message = "{required.validation}")
    String ticketDescription,

    @NotBlank(message = "{required.validation}")
    String ticketPriority,

    @NotBlank(message = "{required.validation}")
    UUID ticketCategory,

    @NotBlank(message = "{required.validation}")
    UUID userId,

    @NotBlank(message = "{required.validation}")
    UUID attibuitedToUserId
) {

}
