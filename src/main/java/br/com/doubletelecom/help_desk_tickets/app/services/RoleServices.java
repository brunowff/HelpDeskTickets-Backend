/**
 * Interface for Role Services.
 * 
 * This interface defines the contract for services related to roles within the
 * Help Desk Tickets application. Implementations of this interface should
 * provide the necessary business logic for managing roles.
 */
package br.com.doubletelecom.help_desk_tickets.app.services;

import java.util.List;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Role;

public interface RoleServices {
    List<Role> findAll();
}
