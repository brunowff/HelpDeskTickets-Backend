package br.com.doubletelecom.help_desk_tickets.app.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.FeedDto;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketServices;

import lombok.AllArgsConstructor;

/*
 * End Points to handle Ticket Requisitions
 */
@RestController("tm")
@AllArgsConstructor
public class TicketController {

    private final TicketServices ticketServices;

    @PostMapping("/ticket")
    public ResponseEntity<Void> createTicket(@RequestBody CreateTicketDto ticketDto, JwtAuthenticationToken token){
        ticketServices.save(ticketDto, token);
        return ResponseEntity.ok().build();
        
    }

    /*
     * End point to generate a Paginated Tickets Feed
     */
    @GetMapping("/feed")
    public ResponseEntity<FeedDto> feed(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int pageSize){
        
        try {
            var tickets = ticketServices.feed(page, pageSize);
            return ResponseEntity.ok(new FeedDto(tickets.getContent(),
                page, pageSize, tickets.getTotalPages(), tickets.getTotalElements()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
  
    }

    /*
     * Deletion End Point for the case of user is an author or admin.
     */
    @DeleteMapping("/tickets/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable("id") String ticketId, JwtAuthenticationToken token){

        ticketServices.deleteTicket(ticketId, token);
        return ResponseEntity.ok().build();

    }

}
