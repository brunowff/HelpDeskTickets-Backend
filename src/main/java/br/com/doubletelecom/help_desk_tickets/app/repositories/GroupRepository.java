/**
 * Repository interface for managing {@link Group} entities.
 * <p>
 * This interface extends {@link JpaRepository} to provide CRUD operations
 * for {@link Group} entities identified by a {@link UUID}.
 * </p>
 * 
 * <p>
 * The {@link Repository} annotation indicates that this interface is a Spring
 * Data repository.
 * </p>
 * 
 * @see JpaRepository
 * @see Group
 * @see UUID
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, UUID> {

}
