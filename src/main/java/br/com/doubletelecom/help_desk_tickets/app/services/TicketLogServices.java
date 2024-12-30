package br.com.doubletelecom.help_desk_tickets.app.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketLogDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketLog;

public interface TicketLogServices {
    public Void save(CreateTicketLogDto ticketLogDto, JwtAuthenticationToken token);
    public TicketLog findById(String ticketLogServicesId, JwtAuthenticationToken token);
    public Void delete(String ticketLogServicesId, JwtAuthenticationToken token);
    public Page<TicketLog> findAll(int page, int pageSize);
    public List<TicketLog> findTicketsLogByTicketId(String ticketId, JwtAuthenticationToken token);
    public List<TicketLog> findTicketLogsByUserId(String userId, JwtAuthenticationToken token);
}
