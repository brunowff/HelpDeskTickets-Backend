package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.FeedItemDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.TicketDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketLog;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketLogRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketTypeRepository;
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
    private final TicketTypeRepository ticketTypeRep;
    private final TicketLogRepository ticketLogRep;

    @Override
    @Transactional
    public Ticket save(@RequestBody @Valid CreateTicketDto ticketDto, JwtAuthenticationToken token){

        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var ticketType = ticketTypeRep.findById(ticketDto.ticketType()).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        try {
            var ticket = new Ticket();
            ticket.setUser(user);
            ticket.setTicketTitle(ticketDto.ticketTitle());
            ticket.setTicketDescription(ticketDto.ticketDescription());
            ticket.setTicketType(ticketType);
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
    public Page<Ticket> findAll(@RequestParam(defaultValue = "0") int page, 
                                @RequestParam(defaultValue = "10") int pageSize){
        
        var tickets = ticketRep.findAll(PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationDateTime"));
        return tickets;

    }

    @Override
    @Transactional
    public Ticket update(@RequestBody @Valid TicketDto ticketDto, JwtAuthenticationToken token){

        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var ticket = ticketRep.findById(ticketDto.ticketId()).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var ticketType = ticketTypeRep.findById(ticketDto.ticketType()).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Only Admin, user attribuited to ticket or the author of the ticket can update it.
        if(user.isAdmin()
            || ticket.getUser().getUserId().equals(UUID.fromString(token.getName()))
            || ticket.getAttribuitedToUser().getUserId().equals(UUID.fromString(token.getName()))
        ){

            

            try {

                ticket.setUser(user);
                ticket.setTicketTitle(ticketDto.ticketTitle());
                ticket.setTicketDescription(ticketDto.ticketDescription());
                ticket.setTicketType(ticketType);
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
            || user.hasGroup(ticket.getTicketType().getDestinationGroup().getGroupId())
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
            || user.hasGroup(ticket.getTicketType().getDestinationGroup().getGroupId())
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
            || user.hasGroup(ticket.getTicketType().getDestinationGroup().getGroupId())
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
    public Ticket updateTicketType(@RequestParam String ticketId, @RequestParam String ticketTypeId, JwtAuthenticationToken token){

        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var ticket = ticketRep.findById(UUID.fromString(ticketId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var ticketType = ticketTypeRep.findById(UUID.fromString(ticketTypeId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(user.isAdmin()
            || user.hasGroup(ticket.getTicketType().getDestinationGroup().getGroupId())
            || ticket.getAttribuitedToUser().getUserId().equals(user.getUserId())
            || ticket.getUser().getUserId().equals(user.getUserId())
        ){
            ticket.setTicketType(ticketType);
            ticketRep.save(ticket);

            var ticketLog = new TicketLog();
            ticketLog.setTicket(ticket);
            ticketLog.setUser(user);
            ticketLog.setLogDescription("Ticket Type updated. " + ticket.toString());
            ticketLogRep.save(ticketLog);
                
            return ticket;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

    }

    @Override
    @Transactional
    public Page<FeedItemDto> feed(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int pageSize){

        var tickets = ticketRep.findAll(PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationDateTime"))
                    .map(ticket -> 
                        new FeedItemDto(
                        ticket.getTicketId(),
                        ticket.getTicketTitle(),
                        ticket.getTicketDescription(),
                        ticket.getTicketType(),
                        ticket.getTicketStatus(),
                        ticket.getTicketPriority(),
                        ticket.getUser(),
                        ticket.getAttribuitedToUser(),
                        Date.from(ticket.getCreationDateTime()), // Convert Instant to Date
                        ticket.getFinalizationDateTime()
                        )
                    );
        
        return tickets;                            
    }
    

    @Override
    @Transactional
    public Page<TicketDto> findTicketsByUserId(@RequestParam String userId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize){

        var user = userRep.findById(UUID.fromString(userId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var tickets = ticketRep.findTicketsByUser(user, PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationDateTime"))
                    .map(ticket -> 
                        new TicketDto(
                        ticket.ticketId(),
                        ticket.ticketTitle(),
                        ticket.ticketDescription(),
                        ticket.ticketStatus(),
                        ticket.ticketPriority(),
                        ticket.ticketType(),
                        ticket.userId(),
                        ticket.attibuitedToUserId(),
                        ticket.creationDateTime(),
                        ticket.finalizationDateTime()
                        )
                    );
        return tickets;

    }

    @Override
    @Transactional
    public Page<TicketDto> findTicketsByAttribuitedToUser(@RequestParam String attribuitedToUserId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize){
        
        var user = userRep.findById(UUID.fromString(attribuitedToUserId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var tickets = ticketRep.findTicketsByAttribuitedToUser(user, PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationDateTime"))
                    .map(ticket -> 
                        new TicketDto(
                        ticket.ticketId(),
                        ticket.ticketTitle(),
                        ticket.ticketDescription(),
                        ticket.ticketStatus(),
                        ticket.ticketPriority(),
                        ticket.ticketType(),
                        ticket.userId(),
                        ticket.attibuitedToUserId(),
                        ticket.creationDateTime(),
                        ticket.finalizationDateTime()
                        )
                    );
        return tickets;

    }


    @Override
    @Transactional
    public Page<TicketDto> findTicketsByTicketTypeId(@RequestParam String ticketTypeId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize){

        var tickets = ticketRep.findTicketsByTicketType(UUID.fromString(ticketTypeId), PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationDateTime"))
                    .map(ticket -> 
                        new TicketDto(
                        ticket.ticketId(),
                        ticket.ticketTitle(),
                        ticket.ticketDescription(),
                        ticket.ticketStatus(),
                        ticket.ticketPriority(),
                        ticket.ticketType(),
                        ticket.userId(),
                        ticket.attibuitedToUserId(),
                        ticket.creationDateTime(),
                        ticket.finalizationDateTime()
                        )
                    );
        return tickets;

    }

    @Override
    @Transactional
    public Page<TicketDto> findTicketsByStatus(@RequestParam String status, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize){

        var tickets = ticketRep.findTicketsByTicketStatus(status, PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationDateTime"))
                    .map(ticket -> 
                        new TicketDto(
                        ticket.ticketId(),
                        ticket.ticketTitle(),
                        ticket.ticketDescription(),
                        ticket.ticketStatus(),
                        ticket.ticketPriority(),
                        ticket.ticketType(),
                        ticket.userId(),
                        ticket.attibuitedToUserId(),
                        ticket.creationDateTime(),
                        ticket.finalizationDateTime()
                        )
                    );
        return tickets;

    }

    @Override
    @Transactional
    public Page<TicketDto> findTicketsByPriority(@RequestParam String priority, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize){

        var tickets = ticketRep.findTicketsByTicketPriority(priority, PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationDateTime"))
                    .map(ticket -> 
                        new TicketDto(
                        ticket.ticketId(),
                        ticket.ticketTitle(),
                        ticket.ticketDescription(),
                        ticket.ticketStatus(),
                        ticket.ticketPriority(),
                        ticket.ticketType(),
                        ticket.userId(),
                        ticket.attibuitedToUserId(),
                        ticket.creationDateTime(),
                        ticket.finalizationDateTime()
                        )
                    );
        return tickets;

    }

    @Override
    @Transactional
    public Page<TicketDto> findTicketsByTitle(@RequestParam String title, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize){

        var tickets = ticketRep.findTicketsByTicketTitleContaining(title, PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationDateTime"))
                    .map(ticket -> 
                        new TicketDto(
                        ticket.ticketId(),
                        ticket.ticketTitle(),
                        ticket.ticketDescription(),
                        ticket.ticketStatus(),
                        ticket.ticketPriority(),
                        ticket.ticketType(),
                        ticket.userId(),
                        ticket.attibuitedToUserId(),
                        ticket.creationDateTime(),
                        ticket.finalizationDateTime()
                        )
                    );
        return tickets;

    }

    @Override
    @Transactional
    public Page<TicketDto> findTicketsByDescription(@RequestParam String description, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int pageSize){

        var tickets = ticketRep.findTicketsByTicketDescriptionContaining(description, PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationDateTime"))
                    .map(ticket -> 
                        new TicketDto(
                        ticket.ticketId(),
                        ticket.ticketTitle(),
                        ticket.ticketDescription(),
                        ticket.ticketStatus(),
                        ticket.ticketPriority(),
                        ticket.ticketType(),
                        ticket.userId(),
                        ticket.attibuitedToUserId(),
                        ticket.creationDateTime(),
                        ticket.finalizationDateTime()
                        )
                    );
        return tickets;

    }

}
