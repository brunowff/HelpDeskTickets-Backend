package br.com.doubletelecom.help_desk_tickets.app.services;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.FeedItemDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;

public interface TicketServices {

    Ticket save(CreateTicketDto ticketDto, JwtAuthenticationToken token);
    Page<FeedItemDto> feed(int page, int pageSize);
    Void deleteTicket(String ticketId, JwtAuthenticationToken token);

}
