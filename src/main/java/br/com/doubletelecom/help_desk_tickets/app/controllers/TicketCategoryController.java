package br.com.doubletelecom.help_desk_tickets.app.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketCategoryDto;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketCategoryServices;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController("/tcm")
@AllArgsConstructor
public class TicketCategoryController {

    private final TicketCategoryServices ticketCategoryServices;

    @PostMapping("/ticket-category")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_CATEGORY_MANAGER')")
    public ResponseEntity<Void> createTicketCategory(@RequestBody @Valid CreateTicketCategoryDto ticketCategoryDto, JwtAuthenticationToken token){
        ticketCategoryServices.save(ticketCategoryDto, token);
        return ResponseEntity.ok().build();
        
    }
}
