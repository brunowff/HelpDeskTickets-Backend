/**
 * Service interface for managing tickets in the Help Desk application.
 * Provides methods for creating, updating, deleting, and retrieving tickets,
 * as well as various search and filter functionalities.
 * 
 * Methods:
 * - save(CreateTicketDto ticketDto, JwtAuthenticationToken token): Creates a new ticket.
 * - feed(int page, int pageSize): Retrieves a paginated feed of ticket items.
 * - deleteTicket(String ticketId, JwtAuthenticationToken token): Deletes a ticket by its ID.
 * - findById(String ticketId): Finds a ticket by its ID.
 * - findAll(int page, int pageSize): Retrieves a paginated list of all tickets.
 * - update(TicketDto ticketDto, JwtAuthenticationToken token): Updates an existing ticket.
 * - updateStatus(String ticketId, String status, JwtAuthenticationToken token): Updates the status of a ticket.
 * - updatePriority(String ticketId, String priority, JwtAuthenticationToken token): Updates the priority of a ticket.
 * - updateAttribuitedTo(String ticketId, String userId, JwtAuthenticationToken token): Updates the user attributed to a ticket.
 * - findTicketsByUserId(String userId, int page, int pageSize): Finds tickets by user ID.
 * - findTicketsByAttribuitedToUser(String userId, int page, int pageSize): Finds tickets attributed to a specific user.
 * - findTicketsByGroupId(String groupId, int page, int pageSize): Finds tickets by group ID.
 * - findTicketsByTicketCategoryId(String ticketCategoryId, int page, int pageSize): Finds tickets by ticket Category ID.
 * - findTicketsByStatus(String status, int page, int pageSize): Finds tickets by status.
 * - findTicketsByPriority(String priority, int page, int pageSize): Finds tickets by priority.
 * - findTicketsByTitle(String title, int page, int pageSize): Finds tickets by title.
 * - findTicketsByDescription(String description, int page, int pageSize): Finds tickets by description.
 * - findByFilter(String userId, String AttibuitedToUserId, String groupId, String ticketCategoryId, String status, String priority, String title, String description, int page, int pageSize): Finds tickets by various filters.
 * 
 * @author
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemTicketDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.TicketDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;

public interface TicketServices {

    public Ticket save(CreateTicketDto ticketDto, JwtAuthenticationToken token);
    public Page<PageItemTicketDto> dashboard(Pageable pageable);
    public Void deleteTicket(String ticketId, JwtAuthenticationToken token);
    public Ticket findById(String ticketId);
    public Page<PageItemTicketDto> findAll(Pageable pageable);
    public Ticket update(TicketDto ticketDto, JwtAuthenticationToken token);
    public Ticket updateStatus(String ticketId, String status, JwtAuthenticationToken token);
    public Ticket updatePriority(String ticketId, String priority, JwtAuthenticationToken token);
    public Ticket updateAttribuitedTo(String ticketId, String userId, JwtAuthenticationToken token);
    public Ticket updateTicketCategory(String ticketId, String ticketCategoryId, JwtAuthenticationToken token);
    public Page<PageItemTicketDto> findTicketsByUserId(String userId, Pageable pageable);
    public Page<PageItemTicketDto> findTicketsByAttribuitedToUser(String userId, Pageable pageable);
    public Page<PageItemTicketDto> findTicketsByTicketCategoryId(String ticketCategoryId, Pageable pageable);
    public Page<PageItemTicketDto> findTicketsByStatus(String status, Pageable pageable);
    public Page<PageItemTicketDto> findTicketsByPriority(String priority, Pageable pageable);
    public Page<PageItemTicketDto> findTicketsByTitle(String title, Pageable pageable);
    public Page<PageItemTicketDto> findTicketsByDescription(String description, Pageable pageable);

}
