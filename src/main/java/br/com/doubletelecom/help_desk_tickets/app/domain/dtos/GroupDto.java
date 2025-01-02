package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GroupDto(
    @NotBlank(message = "{required.validation}")
    UUID groupId,

    @NotBlank(message = "{required.validation}")
    @Size(min = 7, max = 30, message = "{size.validation}")
    String name,

    @NotBlank(message = "{required.validation}")
    @Size(min = 7, max = 300, message = "{size.validation}")
    String description,

    @NotBlank
    Boolean active
) {

}
