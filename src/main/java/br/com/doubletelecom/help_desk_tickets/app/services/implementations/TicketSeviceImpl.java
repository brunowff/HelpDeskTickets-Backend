package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

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
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Role;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.UserRepository;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketServices;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketSeviceImpl implements TicketServices{

    private final TicketRepository ticketRep;
    private final UserRepository userRep;

    @Override
    @Transactional
    public Ticket save(@RequestBody CreateTicketDto ticketDto, JwtAuthenticationToken token){

        var user = userRep.findById(UUID.fromString(token.getName()));
        var ticket = new Ticket();

        ticket.setUser(user.get());
        ticket.setContent(ticketDto.content());

        try {
            ticketRep.save(ticket);
            return ticket;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

    }

    @Override
    @Transactional
    public Page<FeedItemDto> feed(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int pageSize){

        var tickets = ticketRep.findAll(PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimestamp"))
                                .map(ticket -> 
                                    new FeedItemDto(ticket.getTicketId(),
                                    ticket.getContent(),
                                    ticket.getUser().getUsername()));
        
        return tickets;                            
    }
    
    @Override
    @Transactional
    public Void deleteTicket(String ticketId, JwtAuthenticationToken token){

        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var ticket = ticketRep.findById(UUID.fromString(ticketId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        var isAdmin = user.getRoles()
            .stream()
            .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        // Verify if user is an author by the token or if it's an Admin for delete.
        if(isAdmin || ticket.getUser().getUserId().equals(UUID.fromString(token.getName()))){
            ticketRep.delete(ticket);
            return null;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
