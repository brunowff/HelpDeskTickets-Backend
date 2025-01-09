package br.com.doubletelecom.help_desk_tickets.app.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketCategoryDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemTicketCategoryDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.TicketCategoryDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketCategory;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketCategoryServices;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


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

    @PutMapping("/ticket-category/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_CATEGORY_MANAGER')")
    public ResponseEntity<Void> updateTicketCategory(@RequestBody @Valid TicketCategoryDto ticketCategoryDto, JwtAuthenticationToken token){
        ticketCategoryServices.update(ticketCategoryDto, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/ticket-category/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_CATEGORY_MANAGER')")
    public ResponseEntity<Void> deleteTicketCategory(@PathVariable("id") String ticketCategoryId, JwtAuthenticationToken token){
        ticketCategoryServices.delete(ticketCategoryId, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("ticket-category/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_BASIC') or hasAuthority('SCOPE_API_TICKET_CATEGORY')")
    public ResponseEntity<TicketCategory> findById(@PathVariable("id") String ticketCategoryId, JwtAuthenticationToken token){
        var ticketCategory = ticketCategoryServices.findById(ticketCategoryId, token);
        return ResponseEntity.ok(ticketCategory);
    }

    @GetMapping("/ticket-categories")
    @PreAuthorize("hasAuthority('SCOPE_API_BASIC') or hasAuthority('SCOPE_API_TICKET_CATEGORY')")
    public ResponseEntity<Page<PageItemTicketCategoryDto>> findAll(Pageable pageable, JwtAuthenticationToken token){
        var ticketCategories = ticketCategoryServices.findAll(pageable);
        return ResponseEntity.ok(ticketCategories);
    }
    
    @GetMapping("/ticket-category/activate/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_CATEGORY_MANAGER')")
    public ResponseEntity<Void> activateTicketCategory(@PathVariable("id") String ticketCategoryId, JwtAuthenticationToken token){
        ticketCategoryServices.activate(ticketCategoryId, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ticket-category/deactivate/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_CATEGORY_MANAGER')")
    public ResponseEntity<Void> deactivateTicketCategory(@PathVariable("id") String ticketCategoryId, JwtAuthenticationToken token){
        ticketCategoryServices.deactivate(ticketCategoryId, token);
        return ResponseEntity.ok().build();
    }
}
