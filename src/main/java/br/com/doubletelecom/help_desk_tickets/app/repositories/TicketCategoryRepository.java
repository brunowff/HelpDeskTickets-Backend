/**
 * Repository interface for managing {@link TicketCategory} entities.
 * Extends {@link JpaRepository} to provide CRUD operations and more.
 * 
 * @author 
 * @version
 * 
 */
package br.com.doubletelecom.help_desk_tickets.app.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketCategory;

@Repository
public interface TicketCategoryRepository extends JpaRepository<TicketCategory, UUID> {
    
}
