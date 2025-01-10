/**
 * Repository interface for managing {@link TicketMessage} entities.
 * Extends {@link JpaRepository} to provide CRUD operations and pagination.
 * 
 * <p>This repository provides methods to find {@link TicketMessage} entities
 * by {@link Ticket} and {@link User} with pagination support.</p>
 * 
 * @see JpaRepository
 * @see TicketMessage
 * @see Ticket
 * @see User
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketMessage;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;

@Repository
public interface TicketMessageRepository extends JpaRepository<TicketMessage, UUID> {
    public Page<TicketMessage> findByTicket(Ticket ticket, Pageable pageable);
    public Page<TicketMessage> findByUser(User user, Pageable pageable);
}
