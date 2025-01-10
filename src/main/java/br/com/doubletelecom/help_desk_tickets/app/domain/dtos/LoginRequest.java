/**
 * A record representing a login request.
 * 
 * This record is used to encapsulate the email and password fields required for a login request.
 * Both fields are annotated with {@link jakarta.validation.constraints.NotBlank} to ensure they are not blank.
 * The validation message for both fields is specified by the key <code>{required.validation}</code>.
 * 
 * @param email the email address of the user attempting to log in. Must not be blank.
 * @param password the password of the user attempting to log in. Must not be blank.
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank(message = "{required.validation}") String email, 
    @NotBlank(message = "{required.validation}") String password) {
        
}
