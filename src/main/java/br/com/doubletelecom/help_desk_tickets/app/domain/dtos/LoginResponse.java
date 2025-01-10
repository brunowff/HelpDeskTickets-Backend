/**
 * A record representing the response of a login request.
 *
 * @param accessToken   the access token issued upon successful authentication
 * @param expiresIn     the duration (in seconds) for which the access token is valid
 * @param loggedUserDto the details of the logged-in user
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

public record LoginResponse(String accessToken, Long expiresIn, LoggedUserDto loggedUserDto) {

}
