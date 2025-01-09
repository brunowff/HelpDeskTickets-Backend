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
