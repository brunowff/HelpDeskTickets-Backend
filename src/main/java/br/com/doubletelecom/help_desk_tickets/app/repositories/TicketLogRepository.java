package br.com.doubletelecom.help_desk_tickets.app.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketLog;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;

import java.util.List;


@Repository
public interface TicketLogRepository extends JpaRepository<TicketLog, UUID> {

    public List<TicketLog> findByTicket(Ticket ticket);
    public List<TicketLog> findByUser(User user);

}
