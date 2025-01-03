package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.Date;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record TicketLogDto(
    @NotBlank(message = "{required.validation}")
    String logDescription,

    @NotBlank(message = "{required.validation}")    
    UUID userId,

    @NotBlank(message = "{required.validation}")
    UUID ticketId,

    @NotBlank(message = "{required.validation}")
    Date logDateTime
) {

}
