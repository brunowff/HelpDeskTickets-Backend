package br.com.doubletelecom.help_desk_tickets.app.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
    Page<Ticket> findTicketsByUser(User user, Pageable pageable);
    Page<Ticket> findTicketsByAttribuitedToUser(User user, Pageable pageable);

    /**
     * Busca tickets por categoria usando o ID da categoria.
     * O método derivado {@code findByTicketCategory_TicketCategoryId} navega pela
     * associação @ManyToOne corretamente, evitando comparar UUID com entidade.
     */
    Page<Ticket> findByTicketCategory_TicketCategoryId(UUID ticketCategoryId, Pageable pageable);

    Page<Ticket> findTicketsByTicketStatus(String status, Pageable pageable);
    Page<Ticket> findTicketsByTicketPriority(String priority, Pageable pageable);
    Page<Ticket> findTicketsByTicketTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Ticket> findTicketsByTicketDescriptionContainingIgnoreCase(String description, Pageable pageable);
}
