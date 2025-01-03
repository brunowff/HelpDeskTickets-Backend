package br.com.doubletelecom.help_desk_tickets.app.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TickeCategory;

@Repository
public interface TicketCategoryRepository extends JpaRepository<TickeCategory, UUID> {
    
}
