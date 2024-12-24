package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

public record LoginResponse(String accessToken, Long expiresIn, LoggedUserDto loggedUserDto) {

}
