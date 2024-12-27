package br.com.doubletelecom.help_desk_tickets.app.domain.dtos;

import java.util.UUID;

public record FeedItemDto(UUID PostId, String contend, String userName) {

}
