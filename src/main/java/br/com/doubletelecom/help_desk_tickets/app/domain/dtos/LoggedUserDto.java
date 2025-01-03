package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.UUID;

public record LoggedUserDto(UUID id, String username, String fullname, String email) {

}
