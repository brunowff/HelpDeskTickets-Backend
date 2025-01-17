/**
 * Service implementation for managing ticket logs.
 * This class provides methods to save, find, delete, and retrieve ticket logs.
 * It interacts with the UserRepository, TicketLogRepository, and TicketRepository.
 * 
 * Methods:
 * - save(CreateTicketLogDto ticketLogDto, JwtAuthenticationToken token): Saves a new ticket log.
 * - findById(String ticketLogId, JwtAuthenticationToken token): Finds a ticket log by its ID.
 * - delete(String ticketLogId, JwtAuthenticationToken token): Deletes a ticket log by its ID.
 * - findAll(Pageable pageable): Retrieves all ticket logs with pagination.
 * - findTicketsLogByTicket(String ticketId, Pageable pageable): Retrieves ticket logs by ticket ID with pagination.
 * - findTicketLogsByUser(String userId, Pageable pageable): Retrieves ticket logs by user ID with pagination.
 * 
 * Exceptions:
 * - UserNotFoundException: Thrown when a user is not found.
 * - ObjectNotFoundException: Thrown when an object (ticket or ticket log) is not found.
 * - ObjectNotProcessableException: Thrown when an object cannot be processed.
 * - UserNotAuthorizedException: Thrown when a user is not authorized to perform an action.
 * 
 * Annotations:
 * - @Service: Indicates that this class is a service component in the Spring context.
 * - @AllArgsConstructor: Generates a constructor with one parameter for each field in the class.
 * - @Transactional: Indicates that the methods should be executed within a transaction context.
 * - @RequestBody: Indicates that a method parameter should be bound to the body of the web request.
 * - @RequestParam: Indicates that a method parameter should be bound to a web request parameter.
 * - @Validated: Indicates that a method parameter should be validated.
 * 
 * Dependencies:
 * - UserRepository: Repository for managing user entities.
 * - TicketLogRepository: Repository for managing ticket log entities.
 * - TicketRepository: Repository for managing ticket entities.
 * 
 * @see br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketLogDto
 * @see br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemTicketLogDto
 * @see br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketLog
 * @see br.com.doubletelecom.help_desk_tickets.app.exceptions.business.ObjectNotFoundException
 * @see br.com.doubletelecom.help_desk_tickets.app.exceptions.business.ObjectNotProcessableException
 * @see br.com.doubletelecom.help_desk_tickets.app.exceptions.business.UserNotAuthorizedException
 * @see br.com.doubletelecom.help_desk_tickets.app.exceptions.business.UserNotFoundException
 * @see br.com.doubletelecom.help_desk_tickets.app.repositories.TicketRepository
 * @see br.com.doubletelecom.help_desk_tickets.app.repositories.TicketLogRepository
 * @see br.com.doubletelecom.help_desk_tickets.app.repositories.UserRepository
 * @see br.com.doubletelecom.help_desk_tickets.app.services.TicketLogServices
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
import org.springframework.web.bind.annotation.RequestParam;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketLogDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemTicketLogDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketLog;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.ObjectNotFoundException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.ObjectNotProcessableException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.UserNotAuthorizedException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.UserNotFoundException;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketLogRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.UserRepository;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketLogServices;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketLogServiceImpl implements TicketLogServices{
    
    private final UserRepository userRep;
    private final TicketLogRepository ticketLogRep;
    private final TicketRepository ticketRep;

    @Override
    @Transactional
    public TicketLog save(CreateTicketLogDto ticketLogDto, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        var ticket = ticketRep.findById(ticketLogDto.ticketId()).orElseThrow( () -> new ObjectNotFoundException());
        
        try {
            var ticketLog = new TicketLog();
            ticketLog.setTicket(ticket);
            ticketLog.setUser(user);
            ticketLog.setLogDescription(ticketLogDto.logDescription());
            ticketLogRep.save(ticketLog);
            return ticketLog;
        } catch (Exception e) {
            throw new ObjectNotProcessableException();
        }    
    }

    @Override
    @Transactional
    public TicketLog findById(String ticketLogId, JwtAuthenticationToken token){
        
        var ticketLog = ticketLogRep.findById(UUID.fromString(ticketLogId)).orElseThrow( () -> new ObjectNotFoundException());
        
        return ticketLog;
    }

    @Override
    @Transactional
    public Void delete(@RequestParam String ticketLogId, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        
        if(!user.isAdmin()){
            throw new UserNotAuthorizedException();
        }

        var ticketLog = ticketLogRep.findById(UUID.fromString(ticketLogId)).orElseThrow( () -> new ObjectNotFoundException());
        
        try {
            ticketLogRep.delete(ticketLog);
        } catch (Exception e) {
            throw new ObjectNotProcessableException();
        }

        return null;
    }

    @Override
    @Transactional
    public Page<PageItemTicketLogDto> findAll(Pageable pageable){;
        
        var tickets = ticketLogRep.findAll(pageable).map(PageItemTicketLogDto::new);
        return tickets;

    }

    @Override
    @Transactional
    public Page<PageItemTicketLogDto> findTicketsLogByTicketId(String ticketId, Pageable pageable){
        
        var ticket = ticketRep.findById(UUID.fromString(ticketId)).orElseThrow( () -> new ObjectNotFoundException());
        var ticketLogs = ticketLogRep.findByTicket(ticket, pageable).map(PageItemTicketLogDto::new);
        return ticketLogs;

    }

    @Override
    @Transactional
    public Page<PageItemTicketLogDto> findTicketLogsByUserId(String userId, Pageable pageable){

        var user = userRep.findById(UUID.fromString(userId)).orElseThrow( () -> new UserNotFoundException());
        var ticketLogs = ticketLogRep.findByUser(user, pageable).map(PageItemTicketLogDto::new);
        return ticketLogs;

    }
}
