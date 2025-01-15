/**
 * Service implementation for managing Ticket Categories.
 * This class provides methods to create, update, delete, activate, deactivate, and find ticket categories.
 * It also includes authorization checks to ensure that only authorized users can perform certain actions.
 * 
 * Methods:
 * - save(CreateTicketCategoryDto ticketCategoryDto, JwtAuthenticationToken token): Creates a new ticket category.
 * - findById(String ticketCategoryId, JwtAuthenticationToken token): Finds a ticket category by its ID.
 * - delete(String ticketCategoryId, JwtAuthenticationToken token): Deletes a ticket category by its ID.
 * - findAll(Pageable pageable): Finds all ticket categories with pagination.
 * - update(TicketCategoryDto ticketCategoryDto, JwtAuthenticationToken token): Updates an existing ticket category.
 * - activate(String ticketCategoryId, JwtAuthenticationToken token): Activates a ticket category by its ID.
 * - deactivate(String ticketCategoryId, JwtAuthenticationToken token): Deactivates a ticket category by its ID.
 * 
 * Exceptions:
 * - UserNotFoundException: Thrown when the user is not found.
 * - UserNotAuthorizedException: Thrown when the user is not authorized to perform the action.
 * - ObjectNotFoundException: Thrown when the ticket category or group is not found.
 * - ObjectNotActivatedException: Thrown when the group is not activated.
 * - ObjectNotProcessableException: Thrown when the ticket category cannot be processed.
 * 
 * Annotations:
 * - @Service: Indicates that this class is a service component in the Spring context.
 * - @AllArgsConstructor: Generates a constructor with 1 parameter for each field in the class.
 * - @Transactional: Indicates that the methods should be executed within a transaction context.
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketCategoryDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemTicketCategoryDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.TicketCategoryDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketCategory;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.ObjectNotActivatedException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.ObjectNotFoundException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.ObjectNotProcessableException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.UserNotAuthorizedException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.UserNotFoundException;
import br.com.doubletelecom.help_desk_tickets.app.repositories.GroupRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketCategoryRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.UserRepository;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketCategoryServices;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketCategoryServiceImpl implements TicketCategoryServices{

    private final UserRepository userRep;
    private final GroupRepository groupRep;
    private final TicketCategoryRepository ticketCategoryRep;

    @Override
    @Transactional
    public TicketCategory save(@RequestBody @Validated CreateTicketCategoryDto ticketCategoryDto, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        
        if(user.isAdmin() || user.hasRole("API_TICKET_CATEGORY_MANAGER")){
            
            var group = groupRep.findById(ticketCategoryDto.destinationGroup().getGroupId()).orElseThrow( () -> new ObjectNotFoundException());
            
            if(!group.getActive()){
                throw new ObjectNotActivatedException();
            }

            var ticketCategory = new TicketCategory();
    
            try {
                ticketCategory.setDestinationGroup(group);
                ticketCategory.setName(ticketCategoryDto.name());
                ticketCategory.setActive(true);
                ticketCategoryRep.save(ticketCategory);
                return ticketCategory;
            } catch (Exception e) {
                throw new ObjectNotProcessableException();
            }
        } else {
            throw new UserNotAuthorizedException();
        }
 
    }

    @Override
    @Transactional
    public TicketCategory findById(@RequestParam String ticketCategoryId, JwtAuthenticationToken token){
        
        var ticketCategory = ticketCategoryRep.findById(UUID.fromString(ticketCategoryId)).orElseThrow( () -> new ObjectNotFoundException());
        return ticketCategory;
    }

    @Override
    @Transactional
    public Void delete(@RequestParam String ticketCategoryId, JwtAuthenticationToken token){

        // It not recomended to delete a TicketCategory, but just inactivate it.
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());

        if(!user.isAdmin()){
            throw new UserNotAuthorizedException();
        }

        try {
            var ticketCategory = ticketCategoryRep.findById(UUID.fromString(ticketCategoryId)).orElseThrow(() -> new ObjectNotFoundException());
            ticketCategory.setActive(false);
            ticketCategoryRep.save(ticketCategory);
        } catch (Exception e) {
            throw new ObjectNotProcessableException();
        }
        return null;
    }

    @Override
    @Transactional
    public Page<PageItemTicketCategoryDto> findAll(Pageable pageable){
        
        var ticketCategories = ticketCategoryRep.findAll(pageable).map(PageItemTicketCategoryDto::new);
        return ticketCategories;
    }

    @Override
    @Transactional
    public TicketCategory update(@RequestBody @Validated TicketCategoryDto ticketCategoryDto, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        var group = groupRep.findById(ticketCategoryDto.destinationGroup().getGroupId()).orElseThrow( () -> new ObjectNotFoundException());
        
        if(!group.getActive()){
            throw new ObjectNotActivatedException();
        }

        if(user.isAdmin() || user.hasRole("API_TICKET_CATEGORY_MANAGER")){
            try {
                var ticketCategory = ticketCategoryRep.findById(ticketCategoryDto.ticketCategoryId()).orElseThrow( () -> new ObjectNotFoundException());
                ticketCategory.setName(ticketCategoryDto.name());
                ticketCategory.setDestinationGroup(group);
                ticketCategory.setActive(ticketCategoryDto.active());
                ticketCategoryRep.save(ticketCategory);
                return ticketCategory;
            } catch (Exception e) {
                throw new ObjectNotProcessableException();
            }
        } else {
            throw new UserNotAuthorizedException();
        }
        
    }

    @Override
    @Transactional
    public Void activate(@RequestParam String ticketCategoryId, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());

        if(user.isAdmin() || user.hasRole("API_TICKET_CATEGORY_MANAGER")){
            try {
                var ticketCategory = ticketCategoryRep.findById(UUID.fromString(ticketCategoryId)).orElseThrow(() -> new ObjectNotFoundException());
                ticketCategory.setActive(true);
                ticketCategoryRep.save(ticketCategory);
                return null;
            } catch (Exception e) {
                throw new ObjectNotProcessableException();
            }
        } else {
            throw new UserNotAuthorizedException();
        }
    }

    @Override
    @Transactional
    public Void deactivate(@RequestParam String ticketCategoryId, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());

        if(user.isAdmin() || user.hasRole("API_TICKET_CATEGORY_MANAGER")){
            try {
                var ticketCategory = ticketCategoryRep.findById(UUID.fromString(ticketCategoryId)).orElseThrow(() -> new ObjectNotFoundException());
                ticketCategory.setActive(false);
                ticketCategoryRep.save(ticketCategory);
                return null;
            } catch (Exception e) {
                throw new ObjectNotProcessableException();
            }
        } else {
            throw new UserNotAuthorizedException();
        }
    }
}
