package br.com.doubletelecom.help_desk_tickets.app.services;

import java.util.List;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;

public interface TicketLogServices {
    public TicketLogServices save(TicketLogServices ticketLogServices, JwtAuthenticationToken token);
    public TicketLogServices findById(String ticketLogServicesId, JwtAuthenticationToken token);
    public Void delete(String ticketLogServicesId, JwtAuthenticationToken token);
    public List<TicketLogServices> findAll(JwtAuthenticationToken token);
    public List<Ticket> findTicketsLogByTicketId(String ticketId, JwtAuthenticationToken token);
    public List<TicketLogServices> findTicketLogsByUserId(String userId, JwtAuthenticationToken token);
}
