package br.com.doubletelecom.help_desk_tickets.app.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.TicketDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    public Page<TicketDto> findTicketsByUser(User user, Pageable pageable);
    public Page<TicketDto> findTicketsByAttribuitedToUser(User user, Pageable pageable);
    public Page<TicketDto> findTicketsByTicketType(UUID ticketTypeId, Pageable pageable);
    public Page<TicketDto> findTicketsByTicketStatus(String status, Pageable pageable);
    public Page<TicketDto> findTicketsByTicketPriority(String priority, Pageable pageable);
    public Page<TicketDto> findTicketsByTicketTitleContaining(String title, Pageable pageable);
    public Page<TicketDto> findTicketsByTicketDescriptionContaining(String description, Pageable pageable);

}
