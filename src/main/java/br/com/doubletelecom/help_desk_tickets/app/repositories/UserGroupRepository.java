/**
 * Repository interface for managing UserGroup entities.
 * Provides methods to perform CRUD operations and custom queries.
 * 
 * Methods:
 * - findUsersByGroup(Group group): Retrieves a list of users associated with a specific group.
 * - findGroupsByUser(User user): Retrieves a list of groups associated with a specific user.
 * - findByGroup(Group group): Retrieves a list of UserGroup entities associated with a specific group.
 * - findByUserAndGroup(User user, Group group): Retrieves a UserGroup entity based on a specific user and group.
 * 
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see br.com.doubletelecom.help_desk_tickets.app.domain.entities.UserGroup
 * @see br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group
 * @see br.com.doubletelecom.help_desk_tickets.app.domain.entities.User
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.UserGroup;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Group;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;


@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, UUID> {
    public Page<UserGroup> findUsersByGroup(Group group, Pageable pageable);
    public Page<UserGroup> findGroupsByUser(User user, Pageable pageable);
}
