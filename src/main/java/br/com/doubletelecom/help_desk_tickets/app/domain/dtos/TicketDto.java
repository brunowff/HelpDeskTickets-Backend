package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record TicketDto(

    @NotBlank(message = "{required.validation}")
    UUID ticketId,

    @NotBlank(message = "{required.validation}")
    String ticketTitle,

    @NotBlank(message = "{required.validation}")
    String ticketDescription,

    @NotBlank(message = "{required.validation}")
    UUID ticketType,

    @NotBlank(message = "{required.validation}")
    String ticketStatus,
    
    @NotBlank(message = "{required.validation}")
    String ticketPriority,

    @NotBlank(message = "{required.validation}")
    UUID userId,

    @NotBlank(message = "{required.validation}")
    UUID attibuitedToUserId
    
    ) {

}
