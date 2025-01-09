
/**
 * Service interface for managing ticket messages in the Help Desk Tickets application.
 * Provides methods for saving, finding, deleting, and listing ticket messages.
 * 
 * Methods:
 * - save(CreateTicketMessageDto ticketMessage, JwtAuthenticationToken token): Saves a new ticket message.
 * - findById(String ticketMessageId, JwtAuthenticationToken token): Finds a ticket message by its ID.
 * - delete(String ticketMessageId, JwtAuthenticationToken token): Deletes a ticket message by its ID.
 * - findAll(int page, int pageSize): Retrieves a paginated list of all ticket messages.
 * - findTicketMessagesByTicketId(String ticketId, JwtAuthenticationToken token): Finds all ticket messages associated with a specific ticket ID.
 * - findTicketMessagesByUserId(String userId, JwtAuthenticationToken token): Finds all ticket messages associated with a specific user ID.
 * 
 * Note: All methods that require authentication use JwtAuthenticationToken to ensure secure access.
 */
package br.com.doubletelecom.help_desk_tickets.app.services;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketMessageDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemTicketMessageDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketMessage;

public interface TicketMessageServices {
    public TicketMessage save(CreateTicketMessageDto ticketMessage, JwtAuthenticationToken token);
    public TicketMessage findById(String ticketMessageId, JwtAuthenticationToken token);
    public Void delete(String ticketMessageId, JwtAuthenticationToken token);
    public Page<PageItemTicketMessageDto> findAll(Pageable pageable, JwtAuthenticationToken token);
    public Page<PageItemTicketMessageDto> findTicketMessagesByTicketId(String ticketId, Pageable pageable, JwtAuthenticationToken token);
    public Page<PageItemTicketMessageDto> findTicketMessagesByUserId(String userId, Pageable pageable, JwtAuthenticationToken token);
}
