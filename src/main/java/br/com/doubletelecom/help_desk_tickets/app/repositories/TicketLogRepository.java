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
