package br.com.doubletelecom.help_desk_tickets.app.controllers;

import org.springframework.web.bind.annotation.RestController;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketMessageDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemTicketMessageDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketMessage;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketMessageServices;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController("/tmm")
@AllArgsConstructor
public class TicketMessageController {

    private final TicketMessageServices ticketMessageServices;

    @PostMapping("/ticket-message")
    @PreAuthorize("hasAuthority('SCOPE_API_BASIC') or hasAuthority('SCOPE_API_TICKET_MESSAGE')")
    public ResponseEntity<Void> createTicketMessage(@RequestBody @Valid CreateTicketMessageDto ticketMessageDto, JwtAuthenticationToken token) {
        ticketMessageServices.save(ticketMessageDto, token);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/ticket-message/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_MESSAGE_MANAGER')")
    public ResponseEntity<Void> deleteTicketMessge(@PathVariable("id") String ticketMessgeId, JwtAuthenticationToken token) {
        ticketMessageServices.delete(ticketMessgeId, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ticket-messages")
    @PreAuthorize("hasAuthority('SCOPE_API_BASIC') or hasAuthority('SCOPE_API_TICKET_MESSAGE')")
    public ResponseEntity<Page<PageItemTicketMessageDto>> findAll(@PageableDefault(page = 0, size = 20) Pageable pageable, JwtAuthenticationToken token) {

        try {
            var ticketMessages = ticketMessageServices.findAll(pageable, token);
            return ResponseEntity.ok(ticketMessages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/ticket-message/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_BASIC') or hasAuthority('SCOPE_API_TICKET_MESSAGE')")
    public ResponseEntity<TicketMessage> findById(@RequestParam String ticketMessageId, JwtAuthenticationToken token) {
        var ticketMessage = ticketMessageServices.findById(ticketMessageId, token);
        return ResponseEntity.ok(ticketMessage);
    }

    @GetMapping("/ticket-message/ticket/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_BASIC') or hasAuthority('SCOPE_API_TICKET_MESSAGE')")
    public ResponseEntity<Page<PageItemTicketMessageDto>> findByTicketId(@RequestParam String ticketId, Pageable pageable, JwtAuthenticationToken token) {
        try {
            var ticketMessages = ticketMessageServices.findTicketMessagesByTicketId(ticketId, pageable, token);
            return ResponseEntity.ok(ticketMessages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/ticket-message/user/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_BASIC') or hasAuthority('SCOPE_API_TICKET_MESSAGE')")
    public ResponseEntity<Page<PageItemTicketMessageDto>> findByUserId(@RequestParam String userId, Pageable pageable, JwtAuthenticationToken token) {
        try {
            var ticketMessages = ticketMessageServices.findTicketMessagesByUserId(userId, pageable, token);
            return ResponseEntity.ok(ticketMessages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    
}
