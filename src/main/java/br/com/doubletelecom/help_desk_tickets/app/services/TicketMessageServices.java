package br.com.doubletelecom.help_desk_tickets.app.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketMessageDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketMessage;

public interface TicketMessageServices {
    public TicketMessage save(CreateTicketMessageDto ticketMessage, JwtAuthenticationToken token);
    public TicketMessage findById(String ticketMessageId, JwtAuthenticationToken token);
    public Void delete(String ticketMessageId, JwtAuthenticationToken token);
    public Page<TicketMessage> findAll(int page, int pageSize);
    public List<TicketMessage> findTicketMessagesByTicketId(String ticketId, JwtAuthenticationToken token);
    public List<TicketMessage> findTicketMessagesByUserId(String userId, JwtAuthenticationToken token);
}
