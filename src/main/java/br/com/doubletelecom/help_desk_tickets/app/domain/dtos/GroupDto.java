package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

public record GroupDto(
    @NotBlank(message = "{required.validation}")
    UUID groupId,

    @NotBlank(message = "{required.validation}")
    String name
) {

}
