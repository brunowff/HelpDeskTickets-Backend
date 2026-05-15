/**
 * Service implementation for managing ticket messages.
 * This class provides methods to save, find, delete, and retrieve ticket messages.
 * It ensures that only authorized users can perform certain actions.
 * 
 * Dependencies:
 * - TicketRepository
 * - TicketMessageRepository
 * - TicketCategoryRepository
 * - UserRepository
 * 
 * Annotations:
 * - @Service: Indicates that this class is a service component in the Spring context.
 * - @AllArgsConstructor: Generates a constructor with one parameter for each field in the class.
 * - @Transactional: Ensures that methods are executed within a transactional context.
 * 
 * Methods:
 * - save(CreateTicketMessageDto ticketMessageDto, JwtAuthenticationToken token): Saves a new ticket message.
 * - findById(String ticketMessageId, JwtAuthenticationToken token): Finds a ticket message by its ID.
 * - delete(String ticketMessageId, JwtAuthenticationToken token): Deletes a ticket message by its ID.
 * - findAll(Pageable pageable, JwtAuthenticationToken token): Retrieves all ticket messages with pagination.
 * - findTicketMessagesByTicketId(String ticketId, Pageable pageable, JwtAuthenticationToken token): Retrieves ticket messages by ticket ID with pagination.
 * - findTicketMessagesByUserId(String userId, Pageable pageable, JwtAuthenticationToken token): Retrieves ticket messages by user ID with pagination.
 * 
 * Exceptions:
 * - ObjectNotFoundException: Thrown when an object is not found in the repository.
 * - ObjectNotProcessableException: Thrown when an object cannot be processed.
 * - UserNotAuthorizedException: Thrown when a user is not authorized to perform an action.
 * - UserNotFoundException: Thrown when a user is not found in the repository.
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketMessageDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemTicketMessageDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketMessage;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.ObjectNotFoundException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.ObjectNotProcessableException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.UserNotAuthorizedException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.UserNotFoundException;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketMessageRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketCategoryRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.UserRepository;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketMessageServices;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketMessageServiceImpl implements TicketMessageServices{
    
    private final TicketRepository ticketRep;
    private final TicketMessageRepository ticketMessageRep;
    private final TicketCategoryRepository ticketCategoryRep;
    private final UserRepository userRep;
    
    @Override
    @Transactional
    public TicketMessage save(@RequestBody @Validated CreateTicketMessageDto ticketMessageDto, JwtAuthenticationToken token){
        
        // If the user is an admin, is in the destination group of the ticket, or is the user who created the ticket

        var ticket = ticketRep.findById(ticketMessageDto.ticketId()).orElseThrow( () -> new ObjectNotFoundException());
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        var ticketCategory = ticketCategoryRep.findById(ticket.getTicketCategory().getTicketCategoryId()).orElseThrow( () -> new ObjectNotFoundException());
        var isUserInGroup = user.getGroups().stream().anyMatch( userGroup -> userGroup.getName().equals(ticketCategory.getDestinationGroup().getName()));

        if(user.isAdmin() || isUserInGroup || ticket.getUser().getUserId().equals(user.getUserId())){
            try {
                var ticketMessage = new TicketMessage();
                ticketMessage.setTicket(ticket);
                ticketMessage.setUser(user);
                ticketMessage.setMessage(ticketMessageDto.message());
                ticketMessageRep.save(ticketMessage);
                return ticketMessage;
            } catch (Exception e) {
                throw new ObjectNotProcessableException();
            }
        } else {
            throw new UserNotAuthorizedException();
        }
        
    }

    @Override
    @Transactional
    public TicketMessage findById(@RequestParam String ticketMessageId, JwtAuthenticationToken token){
        
        var ticketMessage = ticketMessageRep.findById(UUID.fromString(ticketMessageId)).orElseThrow( () -> new ObjectNotFoundException());
        return ticketMessage;
    }
    
    @Override
    @Transactional
    public Void delete(@RequestParam String ticketMessageId, JwtAuthenticationToken token){
        
        // It is not recommended to delete a ticket because it loses the message history
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());

        if(!user.isAdmin()){
            throw new UserNotAuthorizedException();
        }

        var ticketMessage = ticketMessageRep.findById(UUID.fromString(ticketMessageId)).orElseThrow( () -> new ObjectNotFoundException());

        try {
            ticketMessageRep.delete(ticketMessage);
        } catch ( Exception e) {
            throw new ObjectNotProcessableException();
        }
        
        return null;
    }

    @Override
    @Transactional
    public Page<PageItemTicketMessageDto> findAll(Pageable pageable, JwtAuthenticationToken token){
        // A autorização já foi verificada pelo @PreAuthorize no controller.
        // Não é necessário revalidar roles aqui — evita lógica duplicada e inconsistente.
        return ticketMessageRep.findAll(pageable).map(PageItemTicketMessageDto::new);
    }

    @Override
    @Transactional
    public Page<PageItemTicketMessageDto> findTicketMessagesByTicketId(@RequestParam String ticketId, Pageable pageable, JwtAuthenticationToken token){
        
        var ticket = ticketRep.findById(UUID.fromString(ticketId)).orElseThrow( () -> new ObjectNotFoundException());
        var ticketMessages = ticketMessageRep.findByTicket(ticket, pageable).map(PageItemTicketMessageDto::new);
        return ticketMessages;
    }

    @Override
    @Transactional
    public Page<PageItemTicketMessageDto> findTicketMessagesByUserId(@RequestParam String userId, Pageable pageable, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(userId)).orElseThrow( () -> new UserNotFoundException());
        var ticketMessages = ticketMessageRep.findByUser(user, pageable).map(PageItemTicketMessageDto::new);
        return ticketMessages;
    }
}

