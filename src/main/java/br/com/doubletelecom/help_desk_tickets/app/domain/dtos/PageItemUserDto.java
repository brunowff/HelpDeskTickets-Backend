/**
 * Data Transfer Object (DTO) for representing a paginated item of a User.
 * This DTO is used to transfer user data in a paginated format.
 *
 * @param userId   the unique identifier of the user
 * @param username the username of the user
 * @param fullname the full name of the user
 * @param email    the email address of the user
 * @param active   the active status of the user
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.UUID;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;

public record PageItemUserDto(
        UUID userId,
        String username,
        String fullname,
        String email,
        Boolean active
) {

    public PageItemUserDto(User user) {
        this(user.getUserId(), user.getUsername(), user.getFullname(), user.getEmail(), user.getActive());
    }

}
