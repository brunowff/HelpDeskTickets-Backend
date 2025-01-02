
/**
 * Service interface for managing Ticket Logs.
 * Provides methods for saving, finding, deleting, and retrieving ticket logs.
 * 
 * Methods:
 * - save(CreateTicketLogDto ticketLogDto, JwtAuthenticationToken token): Saves a new ticket log.
 * - findById(String ticketLogServicesId, JwtAuthenticationToken token): Finds a ticket log by its ID.
 * - delete(String ticketLogServicesId, JwtAuthenticationToken token): Deletes a ticket log by its ID.
 * - findAll(int page, int pageSize): Retrieves a paginated list of all ticket logs.
 * - findTicketsLogByTicket(String ticket): Retrieves a list of ticket logs associated with a specific ticket.
 * - findTicketLogsByUser(String user): Retrieves a list of ticket logs associated with a specific user.
 */
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
    public List<TicketLog> findTicketsLogByTicket(String ticket);
    public List<TicketLog> findTicketLogsByUser(String user);
}
