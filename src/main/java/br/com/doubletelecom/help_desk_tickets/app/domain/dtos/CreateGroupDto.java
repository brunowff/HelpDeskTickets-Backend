/**
 * Data Transfer Object for creating a new group.
 * 
 * @param name The name of the group. Must be between 7 and 30 characters and cannot be blank.
 * @param description The description of the group. Must be between 7 and 300 characters and cannot be blank.
 * 
 * @author 
 * @version
 */

package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateGroupDto(
    @NotBlank(message = "{required.validation}")
    @Size(min = 2, max = 30, message = "{size.validation}")
    String name,
    
    @Size(min = 7, max = 300, message = "{size.validation}")
    @NotBlank(message = "{required.validation}")
    String description
) {

}
