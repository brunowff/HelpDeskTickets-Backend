/**
 * Service implementation for managing tickets.
 * This class provides methods to create, update, delete, and retrieve tickets.
 * It also handles ticket status, priority, and category updates.
 * 
 * Methods:
 * - save(CreateTicketDto ticketDto, JwtAuthenticationToken token): Creates a new ticket.
 * - deleteTicket(String ticketId, JwtAuthenticationToken token): Deletes a ticket by its ID.
 * - findById(String ticketId): Retrieves a ticket by its ID.
 * - findAll(Pageable pageable): Retrieves all tickets with pagination.
 * - update(TicketDto ticketDto, JwtAuthenticationToken token): Updates an existing ticket.
 * - updateStatus(String ticketId, String status, JwtAuthenticationToken token): Updates the status of a ticket.
 * - updatePriority(String ticketId, String priority, JwtAuthenticationToken token): Updates the priority of a ticket.
 * - updateAttribuitedTo(String ticketId, String userId, JwtAuthenticationToken token): Updates the user assigned to a ticket.
 * - updateTicketCategory(String ticketId, String ticketCategoryId, JwtAuthenticationToken token): Updates the category of a ticket.
 * - dashboard(Pageable pageable): Retrieves a dashboard view of tickets with pagination.
 * - findTicketsByUserId(String userId, Pageable pageable): Retrieves tickets by user ID with pagination.
 * - findTicketsByAttribuitedToUser(String attribuitedToUserId, Pageable pageable): Retrieves tickets by assigned user ID with pagination.
 * - findTicketsByTicketCategoryId(String ticketCategoryId, Pageable pageable): Retrieves tickets by category ID with pagination.
 * - findTicketsByStatus(String status, Pageable pageable): Retrieves tickets by status with pagination.
 * - findTicketsByPriority(String priority, Pageable pageable): Retrieves tickets by priority with pagination.
 * - findTicketsByTitle(String title, Pageable pageable): Retrieves tickets by title with pagination.
 * - findTicketsByDescription(String description, Pageable pageable): Retrieves tickets by description with pagination.
 * 
 * Exceptions:
 * - ObjectNotFoundException: Thrown when a requested object is not found.
 * - ObjectNotProcessableException: Thrown when an object cannot be processed.
 * - UserNotAuthorizedException: Thrown when a user is not authorized to perform an action.
 * - UserNotFoundException: Thrown when a user is not found.
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
 * - TicketRepository: Repository for managing Ticket entities.
 * - UserRepository: Repository for managing User entities.
 * - TicketCategoryRepository: Repository for managing TicketCategory entities.
 * - TicketLogRepository: Repository for managing TicketLog entities.
 *
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemTicketDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.TicketDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketLog;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.ObjectNotFoundException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.ObjectNotProcessableException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.UserNotAuthorizedException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.UserNotFoundException;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketLogRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketCategoryRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.UserRepository;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketServices;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class TicketSeviceImpl implements TicketServices{

    private final TicketRepository ticketRep;
    private final UserRepository userRep;
    private final TicketCategoryRepository ticketCategoryRep;
    private final TicketLogRepository ticketLogRep;

    @Override
    @Transactional
    public Ticket save(CreateTicketDto ticketDto, JwtAuthenticationToken token){

        // O subject do JWT é o UUID do usuário autenticado
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        
        try {
            var ticketCategory = ticketCategoryRep.findById(ticketDto.ticketCategory().getTicketCategoryId()).orElseThrow( () -> new ObjectNotFoundException());
            var ticket = new Ticket();
            ticket.setUser(user);
            ticket.setTicketTitle(ticketDto.ticketTitle());
            ticket.setTicketDescription(ticketDto.ticketDescription());
            ticket.setTicketCategory(ticketCategory);
            // Valida que a prioridade informada é um valor do enum ValuesOfPriority
            ticket.setTicketPriority(Ticket.ValuesOfPriority.valueOf(ticketDto.ticketPriority()).name());
            // Todo ticket novo começa com status ABERTO
            ticket.setTicketStatus(Ticket.ValuesOfTicketStatus.ABERTO.name());
            ticketRep.save(ticket);

            // Registra automaticamente o evento de criação no histórico do ticket
            var ticketLog = new TicketLog();
            ticketLog.setTicket(ticket);
            ticketLog.setUser(user);
            ticketLog.setLogDescription("Ticket created.");
            ticketLogRep.save(ticketLog);

            return ticket;
        } catch (Exception e) {
            throw new ObjectNotProcessableException();
        }

    }

    
    @Override
    @Transactional
    public Void deleteTicket(String ticketId, JwtAuthenticationToken token){

        // Nota: não é recomendado deletar fisicamente um ticket.
        // O "delete" aqui é um soft delete: muda o status para CANCELADO.

        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        var ticket = ticketRep.findById(UUID.fromString(ticketId)).orElseThrow( () -> new ObjectNotFoundException());
        
        // Apenas o autor do ticket ou um admin pode cancelá-lo
        if(user.isAdmin() || ticket.getUser().getUserId().equals(UUID.fromString(token.getName()))){
            ticket.setTicketStatus(Ticket.ValuesOfTicketStatus.CANCELADO.name());
            ticket.setFinalizationDateTime(Instant.now());
            ticketRep.save(ticket);
            return null;
        } else {
            throw new UserNotAuthorizedException();
        }
    }

    @Override
    @Transactional
    public Ticket findById(String ticketId){
        var ticket = ticketRep.findById(UUID.fromString(ticketId)).orElseThrow( () -> new ObjectNotFoundException());
        return ticket;

    }

    @Override
    @Transactional
    public Page<PageItemTicketDto> findAll(Pageable pageable){
        var tickets = ticketRep.findAll(pageable).map(PageItemTicketDto::new);
        return tickets;

    }

    @Override
    @Transactional
    public Ticket update(TicketDto ticketDto, JwtAuthenticationToken token){

        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        var ticket = ticketRep.findById(ticketDto.ticketId()).orElseThrow( () -> new ObjectNotFoundException());
        var ticketCategory = ticketCategoryRep.findById(ticketDto.ticketCategory().getTicketCategoryId()).orElseThrow( () -> new ObjectNotFoundException());

        // Verifica se o usuário atribuído existe antes de comparar (pode ser null)
        boolean isAttribuited = ticket.getAttribuitedToUser() != null
                && ticket.getAttribuitedToUser().getUserId().equals(UUID.fromString(token.getName()));

        if(user.isAdmin()
            || ticket.getUser().getUserId().equals(UUID.fromString(token.getName()))
            || isAttribuited
        ){
            try {
                ticket.setUser(user);
                ticket.setTicketTitle(ticketDto.ticketTitle());
                ticket.setTicketDescription(ticketDto.ticketDescription());
                ticket.setTicketCategory(ticketCategory);
                ticket.setTicketPriority(null != ticketDto.ticketPriority() ? Ticket.ValuesOfPriority.valueOf(ticketDto.ticketPriority()).name() : ticket.getTicketPriority());
                ticket.setTicketStatus(null != ticketDto.ticketStatus() ? Ticket.ValuesOfTicketStatus.valueOf(ticketDto.ticketStatus()).name() : ticket.getTicketStatus());
                ticketRep.save(ticket);

                var ticketLog = new TicketLog();
                ticketLog.setTicket(ticket);
                ticketLog.setUser(user);
                ticketLog.setLogDescription("Ticket updated by: " + user.getUsername());
                ticketLogRep.save(ticketLog);

                return ticket;
            } catch (Exception e) {
                throw new ObjectNotProcessableException();
            }
            
        } else {
            throw new UserNotAuthorizedException();
        }

    }

    @Override
    @Transactional
    public Ticket updateStatus(String ticketId, String status, JwtAuthenticationToken token){

        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        var ticket = ticketRep.findById(UUID.fromString(ticketId)).orElseThrow( () -> new ObjectNotFoundException());

        // Valida o status recebido contra o enum antes de qualquer operação
        Ticket.ValuesOfTicketStatus newStatus;
        try {
            newStatus = Ticket.ValuesOfTicketStatus.valueOf(status);
        } catch (IllegalArgumentException e) {
            throw new ObjectNotProcessableException();
        }

        // Bloqueia alterações em tickets já finalizados ou cancelados
        if(ticket.getTicketStatus().equals(Ticket.ValuesOfTicketStatus.CANCELADO.name())
            || ticket.getTicketStatus().equals(Ticket.ValuesOfTicketStatus.FINALIZADO.name())){
            throw new UserNotAuthorizedException();
        }

        boolean isAttribuited = ticket.getAttribuitedToUser() != null
                && ticket.getAttribuitedToUser().getUserId().equals(user.getUserId());

        if(!user.isAdmin()
            && !user.hasGroup(ticket.getTicketCategory().getDestinationGroup().getGroupId())
            && !isAttribuited){
            throw new UserNotAuthorizedException();
        }

        var message = "Ticket status updated to " + status + " by " + user.getUsername();

        // Finalizar exige que o ticket tenha um responsável e que seja ele (ou admin) a finalizar
        if(newStatus == Ticket.ValuesOfTicketStatus.FINALIZADO){
            if(ticket.getAttribuitedToUser() == null){
                throw new ObjectNotProcessableException();
            }
            if(!ticket.getAttribuitedToUser().equals(user) && !user.isAdmin()){
                throw new UserNotAuthorizedException();
            }
            ticket.setFinalizationDateTime(Instant.now());
            message = "Ticket finalized by: " + user.getUsername();
        }

        // Cancelar só pode ser feito pelo autor ou admin
        if(newStatus == Ticket.ValuesOfTicketStatus.CANCELADO){
            if(!user.getUserId().equals(ticket.getUser().getUserId()) && !user.isAdmin()){
                throw new UserNotAuthorizedException();
            }
            message = "Ticket canceled by: " + user.getUsername();
            ticket.setFinalizationDateTime(Instant.now());
        }

        ticket.setTicketStatus(status);
        ticketRep.save(ticket);

        // Registra a mudança de status no histórico
        var ticketLog = new TicketLog();
        ticketLog.setTicket(ticket);
        ticketLog.setUser(user);
        ticketLog.setLogDescription(message);
        ticketLogRep.save(ticketLog);

        return ticket;
    }

    @Override
    @Transactional
    public Ticket updatePriority(String ticketId, String priority, JwtAuthenticationToken token){

        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        var ticket = ticketRep.findById(UUID.fromString(ticketId)).orElseThrow( () -> new ObjectNotFoundException());

        // Valida o valor de prioridade contra o enum
        try {
            Ticket.ValuesOfPriority.valueOf(priority);
        } catch (IllegalArgumentException e) {
            throw new ObjectNotProcessableException();
        }

        // Bloqueia alterações em tickets já finalizados ou cancelados
        if(ticket.getTicketStatus().equals(Ticket.ValuesOfTicketStatus.CANCELADO.name())
            || ticket.getTicketStatus().equals(Ticket.ValuesOfTicketStatus.FINALIZADO.name())){
            throw new UserNotAuthorizedException();
        }

        boolean isAttribuited = ticket.getAttribuitedToUser() != null
                && ticket.getAttribuitedToUser().getUserId().equals(UUID.fromString(token.getName()));

        if(user.isAdmin()
            || user.hasGroup(ticket.getTicketCategory().getDestinationGroup().getGroupId())
            || isAttribuited
            || ticket.getUser().getUserId().equals(user.getUserId()))
        {
            ticket.setTicketPriority(priority);
            ticketRep.save(ticket);

            var ticketLog = new TicketLog();
            ticketLog.setTicket(ticket);
            ticketLog.setUser(user);
            ticketLog.setLogDescription("Ticket priority updated to " + priority + " by: " + user.getUsername());
            ticketLogRep.save(ticketLog);

            return ticket;
        } else {
            throw new UserNotAuthorizedException();
        }
    }

    @Override
    @Transactional
    public Ticket updateTicketCategory(String ticketId, String ticketCategoryId, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        var ticket = ticketRep.findById(UUID.fromString(ticketId)).orElseThrow( () -> new ObjectNotFoundException());
        var ticketCategory = ticketCategoryRep.findById(UUID.fromString(ticketCategoryId)).orElseThrow( () -> new ObjectNotFoundException());

        boolean isAttribuited = ticket.getAttribuitedToUser() != null
                && ticket.getAttribuitedToUser().getUserId().equals(user.getUserId());

        if(user.isAdmin()
        || user.hasGroup(ticket.getTicketCategory().getDestinationGroup().getGroupId())
        || isAttribuited
        || ticket.getUser().getUserId().equals(user.getUserId())
        ){
            ticket.setTicketCategory(ticketCategory);
            ticketRep.save(ticket);
            
            var ticketLog = new TicketLog();
            ticketLog.setTicket(ticket);
            ticketLog.setUser(user);
            ticketLog.setLogDescription("Ticket Category updated by: " + user.getUsername());
            ticketLogRep.save(ticketLog);
            
            return ticket;
        } else {
            throw new UserNotAuthorizedException();
        }
        
    }
    
    @Override
    @Transactional
    public Ticket updateAttribuitedTo(String ticketId, String userId, JwtAuthenticationToken token){

        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new UserNotFoundException());
        var ticket = ticketRep.findById(UUID.fromString(ticketId)).orElseThrow( () -> new ObjectNotFoundException());
        var user2attribuite = userRep.findById(UUID.fromString(userId)).orElseThrow( () -> new UserNotFoundException());

        boolean isAttribuited = ticket.getAttribuitedToUser() != null
                && ticket.getAttribuitedToUser().getUserId().equals(user.getUserId());

        if(user.isAdmin()
            || user.hasGroup(ticket.getTicketCategory().getDestinationGroup().getGroupId())
            || isAttribuited
            || ticket.getUser().getUserId().equals(user.getUserId())
        ){
            ticket.setAttribuitedToUser(user2attribuite);
            ticketRep.save(ticket);

            var ticketLog = new TicketLog();
            ticketLog.setTicket(ticket);
            ticketLog.setUser(user);
            ticketLog.setLogDescription("Ticket attributed to " + user2attribuite.getUsername() + " by " + user.getUsername());
            ticketLogRep.save(ticketLog);

            return ticket;
        } else {
            throw new UserNotAuthorizedException();
        }
    }

    @Override
    @Transactional
    public Page<PageItemTicketDto> dashboard(Pageable pageable){
        var tickets = ticketRep.findAll(pageable).map(PageItemTicketDto::new);
        return tickets;                            
    }

    @Override
    @Transactional
    public Page<PageItemTicketDto> findTicketsByUserId(String userId, Pageable pageable){

        var user = userRep.findById(UUID.fromString(userId)).orElseThrow( () -> new UserNotFoundException());
        var tickets = ticketRep.findTicketsByUser(user, pageable).map(PageItemTicketDto::new);
        return tickets;

    }

    @Override
    @Transactional
    public Page<PageItemTicketDto> findTicketsByAttribuitedToUser(String attribuitedToUserId, Pageable pageable){
        
        var user = userRep.findById(UUID.fromString(attribuitedToUserId)).orElseThrow( () -> new UserNotFoundException());
        var tickets = ticketRep.findTicketsByAttribuitedToUser(user, pageable).map(PageItemTicketDto::new);
        return tickets;

    }


    @Override
    @Transactional
    public Page<PageItemTicketDto> findTicketsByTicketCategory(String ticketCategoryId, Pageable pageable){
        var tickets = ticketRep.findByTicketCategory_TicketCategoryId(UUID.fromString(ticketCategoryId), pageable).map(PageItemTicketDto::new);
        return tickets;
    }

    @Override
    @Transactional
    public Page<PageItemTicketDto> findTicketsByStatus(String status, Pageable pageable){
        var tickets = ticketRep.findTicketsByTicketStatus(status, pageable).map(PageItemTicketDto::new);
        return tickets;
    }

    @Override
    @Transactional
    public Page<PageItemTicketDto> findTicketsByPriority(String priority, Pageable pageable){
        var tickets = ticketRep.findTicketsByTicketPriority(priority, pageable).map(PageItemTicketDto::new);
        return tickets;
    }

    @Override
    @Transactional
    public Page<PageItemTicketDto> findTicketsByTitle(String title, Pageable pageable){
        var tickets = ticketRep.findTicketsByTicketTitleContainingIgnoreCase(title, pageable).map(PageItemTicketDto::new);
        return tickets;
    }

    @Override
    @Transactional
    public Page<PageItemTicketDto> findTicketsByDescription(String description, Pageable pageable){
        var tickets = ticketRep.findTicketsByTicketDescriptionContainingIgnoreCase(description, pageable).map(PageItemTicketDto::new);
        return tickets;
    }

}
