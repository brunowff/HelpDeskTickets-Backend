
/**
 * The RoleServices interface provides a contract for service methods 
 * related to Role entities. Implementations of this interface are 
 * responsible for handling the business logic associated with Role 
 * entities.
 * 
 * <p>This interface includes methods for retrieving all Role entities.</p>
 * 
 * @see br.com.doubletelecom.help_desk_tickets.app.domain.entities.Role
 * 
 * @author
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.services;

import java.util.List;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Role;

public interface RoleServices {
    public List<Role> findAll();

}
