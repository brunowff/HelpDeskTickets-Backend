/**
 * Repository interface for accessing and managing {@link Role} entities in the database.
 * Extends {@link JpaRepository} to provide CRUD operations and additional query methods.
 * 
 * <p>This repository is responsible for handling data access related to {@link Role} entities.
 * It includes a custom query method to find a role by its name.</p>
 * 
 * @author
 * @version 
 */
package br.com.doubletelecom.help_desk_tickets.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

    public Optional<Role> findByName(String roleName);

}
