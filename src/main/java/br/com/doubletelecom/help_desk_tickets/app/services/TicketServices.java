package br.com.doubletelecom.help_desk_tickets.app.services;

import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.FeedItemDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.TicketDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;

public interface TicketServices {

    public Ticket save(CreateTicketDto ticketDto, JwtAuthenticationToken token);
    public Page<FeedItemDto> feed(int page, int pageSize);
    public Void deleteTicket(String ticketId, JwtAuthenticationToken token);
    public Ticket findById(String ticketId);
    public Page<Ticket> findAll(int page, int pageSize);
    public Ticket update(TicketDto ticketDto, JwtAuthenticationToken token);
    public Page<TicketDto> findTicketsByUserId(String userId, int page, int pageSize);
    public Page<TicketDto> findTicketsByAttribuitedToUser(String userId, int page, int pageSize);
    public Page<TicketDto> findTicketsByGroupId(String groupId, int page, int pageSize);
    public Page<TicketDto> findTicketsByTicketTypeId(String ticketTypeId, int page, int pageSize);
    public Page<TicketDto> findTicketsByStatus(String status, int page, int pageSize);
    public Page<TicketDto> findTicketsByPriority(String priority, int page, int pageSize);
    public Page<TicketDto> findTicketsByTitle(String title, int page, int pageSize);
    public Page<TicketDto> findTicketsByDescription(String description, int page, int pageSize);
    public Page<TicketDto> findByFilter(String userId, String AttibuitedToUserId, String groupId, String ticketTypeId, String status, String priority, String title, String description, int page, int pageSize);

}
