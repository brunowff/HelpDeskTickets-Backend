package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemTicketDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.TicketDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketLog;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketLogRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketCategoryRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.UserRepository;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketServices;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
    public Ticket save(@RequestBody @Valid CreateTicketDto ticketDto, JwtAuthenticationToken token){

        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var ticketCategory = ticketCategoryRep.findById(ticketDto.ticketCategory()).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        try {
            var ticket = new Ticket();
            ticket.setUser(user);
            ticket.setTicketTitle(ticketDto.ticketTitle());
            ticket.setTicketDescription(ticketDto.ticketDescription());
            ticket.setTicketCategory(ticketCategory);
            ticket.setTicketPriority(ticketDto.ticketPriority());
            ticket.setTicketStatus(Ticket.ValuesOfTicketStatus.ABERTO.name());
            ticketRep.save(ticket);

            var ticketLog = new TicketLog();
            ticketLog.setTicket(ticket);
            ticketLog.setUser(user);
            ticketLog.setLogDescription("Ticket created.");
            ticketLogRep.save(ticketLog);

            return ticket;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    
    @Override
    @Transactional
    public Void deleteTicket(@RequestParam String ticketId, JwtAuthenticationToken token){

        // It's not recomended to delete a ticket, but just inactivate it.

        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var ticket = ticketRep.findById(UUID.fromString(ticketId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        // Verify if user is an author by the token or if it's an Admin for delete.
        if(user.isAdmin() || ticket.getUser().getUserId().equals(UUID.fromString(token.getName()))){
            ticketRep.delete(ticket);
            return null;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public Ticket findById(@RequestParam String ticketId){
       
        var ticket = ticketRep.findById(UUID.fromString(ticketId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
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
    public Ticket update(@RequestBody @Valid TicketDto ticketDto, JwtAuthenticationToken token){

        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var ticket = ticketRep.findById(ticketDto.ticketId()).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var ticketCategory = ticketCategoryRep.findById(ticketDto.ticketCategory()).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Only Admin, user attribuited to ticket or the author of the ticket can update it.
        if(user.isAdmin()
            || ticket.getUser().getUserId().equals(UUID.fromString(token.getName()))
            || ticket.getAttribuitedToUser().getUserId().equals(UUID.fromString(token.getName()))
        ){

            

            try {

                ticket.setUser(user);
                ticket.setTicketTitle(ticketDto.ticketTitle());
                ticket.setTicketDescription(ticketDto.ticketDescription());
                ticket.setTicketCategory(ticketCategory);
                ticketRep.save(ticket);

                var ticketLog = new TicketLog();
                ticketLog.setTicket(ticket);
                ticketLog.setUser(user);
                ticketLog.setLogDescription("Ticket updated. " + ticket.toString());
                ticketLogRep.save(ticketLog);

                return ticket;
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
            
            
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

    }

    @Override
    @Transactional
    public Ticket updateStatus(@RequestParam String ticketId, @RequestParam String status, JwtAuthenticationToken token){

        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var ticket = ticketRep.findById(UUID.fromString(ticketId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(user.isAdmin()
            || user.hasGroup(ticket.getTicketCategory().getDestinationGroup().getGroupId())
            || ticket.getAttribuitedToUser().getUserId().equals(user.getUserId())
        ){
            ticket.setTicketStatus(status);
            ticketRep.save(ticket);

            var ticketLog = new TicketLog();
            ticketLog.setTicket(ticket);
            ticketLog.setUser(user);
            ticketLog.setLogDescription("Ticket Status updated. " + ticket.toString());
            ticketLogRep.save(ticketLog);

            return ticket;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

    }

    @Override
    @Transactional
    public Ticket updatePriority(@RequestParam String ticketId, @RequestParam String priority, JwtAuthenticationToken token){

        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var ticket = ticketRep.findById(UUID.fromString(ticketId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(user.isAdmin()
            || user.hasGroup(ticket.getTicketCategory().getDestinationGroup().getGroupId())
            || ticket.getAttribuitedToUser().getUserId().equals(UUID.fromString(token.getName()))
            || ticket.getUser().getUserId().equals(user.getUserId())
        ){
            ticket.setTicketPriority(priority);
            ticketRep.save(ticket);

            var ticketLog = new TicketLog();
            ticketLog.setTicket(ticket);
            ticketLog.setUser(user);
            ticketLog.setLogDescription("Ticket priority updated. " + ticket.toString());
            ticketLogRep.save(ticketLog);

            return ticket;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

    }

    @Override
    @Transactional
    public Ticket updateAttribuitedTo(@RequestParam String ticketId, @RequestParam String userId, JwtAuthenticationToken token){

        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var ticket = ticketRep.findById(UUID.fromString(ticketId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var user2attribuite = userRep.findById(UUID.fromString(userId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(user.isAdmin()
            || user.hasGroup(ticket.getTicketCategory().getDestinationGroup().getGroupId())
            || ticket.getAttribuitedToUser().getUserId().equals(user.getUserId())
            || ticket.getUser().getUserId().equals(user.getUserId())
        ){
            ticket.setAttribuitedToUser(user2attribuite);
            ticketRep.save(ticket);

            var ticketLog = new TicketLog();
            ticketLog.setTicket(ticket);
            ticketLog.setUser(user);
            ticketLog.setLogDescription("Ticket Attribuition updated. " + ticket.toString());
            ticketLogRep.save(ticketLog);

            return ticket;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

    }

    @Override
    @Transactional
    public Ticket updateTicketCategory(@RequestParam String ticketId, @RequestParam String ticketCategoryId, JwtAuthenticationToken token){

        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var ticket = ticketRep.findById(UUID.fromString(ticketId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var ticketCategory = ticketCategoryRep.findById(UUID.fromString(ticketCategoryId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(user.isAdmin()
            || user.hasGroup(ticket.getTicketCategory().getDestinationGroup().getGroupId())
            || ticket.getAttribuitedToUser().getUserId().equals(user.getUserId())
            || ticket.getUser().getUserId().equals(user.getUserId())
        ){
            ticket.setTicketCategory(ticketCategory);
            ticketRep.save(ticket);

            var ticketLog = new TicketLog();
            ticketLog.setTicket(ticket);
            ticketLog.setUser(user);
            ticketLog.setLogDescription("Ticket Category updated. " + ticket.toString());
            ticketLogRep.save(ticketLog);
                
            return ticket;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
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
    public Page<PageItemTicketDto> findTicketsByUserId(@RequestParam String userId, Pageable pageable){

        var user = userRep.findById(UUID.fromString(userId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var tickets = ticketRep.findTicketsByUser(user, pageable).map(PageItemTicketDto::new);
        return tickets;

    }

    @Override
    @Transactional
    public Page<PageItemTicketDto> findTicketsByAttribuitedToUser(@RequestParam String attribuitedToUserId, Pageable pageable){
        
        var user = userRep.findById(UUID.fromString(attribuitedToUserId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var tickets = ticketRep.findTicketsByAttribuitedToUser(user, pageable).map(PageItemTicketDto::new);
        return tickets;

    }


    @Override
    @Transactional
    public Page<PageItemTicketDto> findTicketsByTicketCategoryId(@RequestParam String ticketCategoryId, Pageable pageable){
        var tickets = ticketRep.findTicketsByTicketCategory(UUID.fromString(ticketCategoryId), pageable).map(PageItemTicketDto::new);
        return tickets;

    }

    @Override
    @Transactional
    public Page<PageItemTicketDto> findTicketsByStatus(@RequestParam String status, Pageable pageable){

        var tickets = ticketRep.findTicketsByTicketStatus(status, pageable).map(PageItemTicketDto::new);
        return tickets;

    }

    @Override
    @Transactional
    public Page<PageItemTicketDto> findTicketsByPriority(@RequestParam String priority, Pageable pageable){

        var tickets = ticketRep.findTicketsByTicketPriority(priority, pageable).map(PageItemTicketDto::new);
        return tickets;

    }

    @Override
    @Transactional
    public Page<PageItemTicketDto> findTicketsByTitle(@RequestParam String title, Pageable pageable){

        var tickets = ticketRep.findTicketsByTicketTitleContaining(title, pageable).map(PageItemTicketDto::new);
        return tickets;

    }

    @Override
    @Transactional
    public Page<PageItemTicketDto> findTicketsByDescription(@RequestParam String description, Pageable pageable){

        var tickets = ticketRep.findTicketsByTicketDescriptionContaining(description, pageable).map(PageItemTicketDto::new);
        return tickets;

    }

}
