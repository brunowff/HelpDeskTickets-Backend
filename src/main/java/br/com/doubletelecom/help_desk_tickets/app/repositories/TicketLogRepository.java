/**
 * Repository interface for managing {@link TicketLog} entities.
 * Extends {@link JpaRepository} to provide CRUD operations and pagination support.
 * 
 * <p>This repository provides methods to find {@link TicketLog} entries by {@link Ticket} and {@link User}.</p>
 * 
 * <p>Methods:</p>
 * <ul>
 *   <li>{@link #findByTicket(Ticket, Pageable)}: Retrieves a paginated list of {@link TicketLog} entries associated with a specific {@link Ticket}.</li>
 *   <li>{@link #findByUser(User, Pageable)}: Retrieves a paginated list of {@link TicketLog} entries associated with a specific {@link User}.</li>
 * </ul>
 * 
 * @see TicketLog
 * @see Ticket
 * @see User
 * @see JpaRepository
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
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketLog;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;


@Repository
public interface TicketLogRepository extends JpaRepository<TicketLog, UUID> {

    public Page<TicketLog> findByTicket(Ticket ticket, Pageable pageable);
    public Page<TicketLog> findByUser(User user, Pageable pageable);

}
