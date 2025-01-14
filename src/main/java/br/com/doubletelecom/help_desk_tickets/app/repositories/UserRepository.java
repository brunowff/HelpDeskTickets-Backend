/**
 * Repository interface for {@link User} entities.
 * Extends {@link JpaRepository} to provide CRUD operations and additional query methods.
 * 
 * <p>This repository provides methods to find users by their username or email.</p>
 * 
 * <p>Methods:</p>
 * <ul>
 *   <li>{@link #findByUsername(String)}: Finds a user by their username.</li>
 *   <li>{@link #findByEmail(String)}: Finds a user by their email.</li>
 * </ul>
 * 
 * @see JpaRepository
 * @see User
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>{

    public Optional<User> findByUsername(String username);
    public Optional<User> findByEmail(String email);
}
