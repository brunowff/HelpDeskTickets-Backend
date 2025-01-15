/**
 * Data Transfer Object for User.
 * 
 * @param userId   Unique identifier for the user. Must not be blank.
 * @param fullname Full name of the user. Must not be blank.
 * @param username Username of the user. Must be between 7 and 30 characters. Must not be blank.
 * @param email    Email address of the user. Must be a valid email format. Must not be blank.
 * @param password Password for the user. Must be between 6 and 30 characters. Must not be blank.
 * @param active   Indicates whether the user is active. Must not be blank.
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.UUID;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDto(

    @NotNull(message = "{required.validation}")
    UUID userId,

    @NotBlank(message = "{required.validation}")
    String fullname,

    @NotBlank(message = "{required.validation}")
    @Size(min = 7, max = 30, message = "{size.validation}")
    String username,

    @NotBlank(message = "{required.validation}")
    @Email(message = "{email.validation}")
    String email, 

    @NotNull(message = "{required.validation}")
    Boolean active
    
    ) {

        public UserDto(){
            this(null, null, null, null, null);
        }

        public UserDto(User user) {
            this(user.getUserId(), user.getFullname(), user.getUsername(), user.getEmail(), user.getActive());
        }
}
