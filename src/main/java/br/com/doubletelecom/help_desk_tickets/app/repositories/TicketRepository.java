/**
 * Repository interface for managing Ticket entities.
 * Extends JpaRepository to provide CRUD operations and pagination support.
 * 
 * Methods:
 * - findTicketsByUser(User user, Pageable pageable): Retrieves a paginated list of tickets created by the specified user.
 * - findTicketsByAttribuitedToUser(User user, Pageable pageable): Retrieves a paginated list of tickets assigned to the specified user.
 * - findTicketsByTicketCategory(UUID ticketCategoryId, Pageable pageable): Retrieves a paginated list of tickets belonging to the specified category.
 * - findTicketsByTicketStatus(String status, Pageable pageable): Retrieves a paginated list of tickets with the specified status.
 * - findTicketsByTicketPriority(String priority, Pageable pageable): Retrieves a paginated list of tickets with the specified priority.
 * - findTicketsByTicketTitleContaining(String title, Pageable pageable): Retrieves a paginated list of tickets with titles containing the specified keyword.
 * - findTicketsByTicketDescriptionContaining(String description, Pageable pageable): Retrieves a paginated list of tickets with descriptions containing the specified keyword.
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    public Page<Ticket> findTicketsByUser(User user, Pageable pageable);
    public Page<Ticket> findTicketsByAttribuitedToUser(User user, Pageable pageable);
    public Page<Ticket> findTicketsByTicketCategory(UUID ticketCategoryId, Pageable pageable);
    public Page<Ticket> findTicketsByTicketStatus(String status, Pageable pageable);
    public Page<Ticket> findTicketsByTicketPriority(String priority, Pageable pageable);
    public Page<Ticket> findTicketsByTicketTitleContaining(String title, Pageable pageable);
    public Page<Ticket> findTicketsByTicketDescriptionContaining(String description, Pageable pageable);

}
