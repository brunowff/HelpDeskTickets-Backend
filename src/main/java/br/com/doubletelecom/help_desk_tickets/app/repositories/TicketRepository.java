package br.com.doubletelecom.help_desk_tickets.app.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {

}
