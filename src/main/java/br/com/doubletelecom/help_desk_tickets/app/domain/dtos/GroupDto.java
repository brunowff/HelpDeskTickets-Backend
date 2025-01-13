/**
 * Data Transfer Object (DTO) for Group entity.
 * This class is used to transfer group data between processes.
 * 
 * @param groupId     Unique identifier for the group. Must not be blank.
 * @param name        Name of the group. Must be between 7 and 30 characters long and must not be blank.
 * @param description Description of the group. Must be between 7 and 300 characters long and must not be blank.
 * @param active      Indicates whether the group is active. Must not be blank.
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GroupDto(
    @NotNull(message = "{required.validation}")
    UUID groupId,

    @NotBlank(message = "{required.validation}")
    @Size(min = 7, max = 30, message = "{size.validation}")
    String name,

    @NotBlank(message = "{required.validation}")
    @Size(min = 7, max = 300, message = "{size.validation}")
    String description,

    @NotNull(message = "{required.validation}")
    Boolean active
) {

}
