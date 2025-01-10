/**
 * A Data Transfer Object (DTO) representing a logged-in user.
 * 
 * @param id       the unique identifier of the user
 * @param username the username of the user
 * @param fullname the full name of the user
 * @param email    the email address of the user
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.UUID;

public record LoggedUserDto(UUID id, String username, String fullname, String email) {

}
