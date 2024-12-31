package br.com.doubletelecom.help_desk_tickets.app.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketType;

@Repository
public interface TicketTypeRepository extends JpaRepository<TicketType, UUID> {
    public List<Ticket> findByTicket(Ticket ticket);
}
