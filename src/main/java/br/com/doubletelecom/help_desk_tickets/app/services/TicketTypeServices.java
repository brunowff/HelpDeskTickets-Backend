package br.com.doubletelecom.help_desk_tickets.app.services;

import java.util.List;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketTypeDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketType;

public interface TicketTypeServices {
    public TicketType save(CreateTicketTypeDto ticketTypeDto, JwtAuthenticationToken token);
    public TicketType findById(String ticketTypeId, JwtAuthenticationToken token);
    public Void delete(String ticketTypeId, JwtAuthenticationToken token);
    public List<TicketType> findAll();
    public List<Ticket> findTicketsByTicketTypeId(String ticketTypeId, JwtAuthenticationToken token);
    public TicketType update(CreateTicketTypeDto ticketTypeDto, JwtAuthenticationToken token);
}
