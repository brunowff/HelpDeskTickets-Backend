
/**
 * Data Transfer Object for creating a new user.
 * This class is used to encapsulate the data required to create a new user.
 * 
 * Fields:
 * - username: The username of the user. Must be between 7 and 30 characters long and cannot be blank.
 * - email: The email address of the user. Must be a valid email format and cannot be blank.
 * - password: The password of the user. Must be between 6 and 30 characters long and cannot be blank.
 * - fullname: The full name of the user. Cannot be blank.
 * 
 * Annotations:
 * - @NotBlank: Ensures the field is not null and not empty.
 * - @Size: Ensures the field's length is within the specified range.
 * - @Email: Ensures the field is a valid email format.
 * 
 * Validation messages are specified using message keys for internationalization.
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;

public record CreateUserDto(
    @NotBlank(message = "{required.validation}")
    @Size(min = 7, max = 30, message = "{size.validation}")
    String username,

    @NotBlank(message = "{required.validation}")
    @Email(message = "{email.validation}")
    String email, 
    
    @NotBlank(message = "{required.validation}")
    @Size(min = 6, max = 30, message = "{size.validation}")
    String password, 
    
    @NotBlank(message = "{required.validation}")
    String fullname) {

}
