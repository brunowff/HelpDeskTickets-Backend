package br.com.doubletelecom.help_desk_tickets.app.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemTicketDto;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketServices;

import lombok.AllArgsConstructor;

@RestController("/tm")
@AllArgsConstructor
public class TicketController {
    
    //TODO: Implement endpoints to handle ticket requisitions

    private final TicketServices ticketServices;

    @PostMapping("/ticket")
    public ResponseEntity<Void> createTicket(@RequestBody CreateTicketDto ticketDto, JwtAuthenticationToken token){
        ticketServices.save(ticketDto, token);
        return ResponseEntity.ok().build();
        
    }

    @GetMapping("/dashboard")
    public ResponseEntity<Page<PageItemTicketDto>> dashboard(Pageable pageable){
        
        try {
            var tickets = ticketServices.dashboard(pageable);
            return ResponseEntity.ok(tickets);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
  
    }

    @DeleteMapping("/tickets/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable("id") String ticketId, JwtAuthenticationToken token){

        ticketServices.deleteTicket(ticketId, token);
        return ResponseEntity.ok().build();

    }

}
