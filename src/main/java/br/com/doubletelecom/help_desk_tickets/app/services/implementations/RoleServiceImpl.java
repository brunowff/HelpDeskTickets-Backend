/**
 * Service implementation for managing roles.
 * This class provides methods to interact with the RoleRepository.
 * It is annotated with @Service to indicate that it's a service component in the Spring context.
 * The @AllArgsConstructor annotation is used to generate a constructor with one parameter for each field in the class.
 * 
 * This class implements the RoleServices interface.
 * 
 * Methods:
 * - findAll(): Retrieves all roles from the repository.
 * 
 * Dependencies:
 * - RoleRepository: Repository for Role entities.
 * 
 * Annotations:
 * - @Service: Indicates that this class is a service component.
 * - @AllArgsConstructor: Generates a constructor with one parameter for each field.
 * - @Transactional: Ensures that the findAll method is executed within a transaction.
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.List;
import org.springframework.stereotype.Service;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Role;
import br.com.doubletelecom.help_desk_tickets.app.repositories.RoleRepository;
import br.com.doubletelecom.help_desk_tickets.app.services.RoleServices;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleServices{

    private final RoleRepository roleRep;

    @Override
    @Transactional
    public List<Role> findAll(){
        return roleRep.findAll();
    }

}
