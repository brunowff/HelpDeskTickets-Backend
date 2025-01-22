/**
 * Controller for managing ticket categories.
 * Provides endpoints for creating, updating, deleting, finding, activating, and deactivating ticket categories.
 * 
 * Endpoints:
 * - POST /ticket-categories: Create a new ticket category.
 * - PUT /ticket-categories/{id}: Update an existing ticket category.
 * - DELETE /ticket-categories/{id}: Delete a ticket category.
 * - GET /ticket-category/{id}: Find a ticket category by ID.
 * - GET /ticket-categories: Find all ticket categories with pagination.
 * - GET /ticket-categories/activate/{id}: Activate a ticket category.
 * - GET /ticket-categories/deactivate/{id}: Deactivate a ticket category.
 * 
 * Security:
 * - Requires appropriate authority scopes for each endpoint.
 * 
 * @author 
 * @version 
 */

package br.com.doubletelecom.help_desk_tickets.app.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketCategoryDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemTicketCategoryDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.TicketCategoryDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketCategory;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketCategoryServices;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@AllArgsConstructor
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/ticket-category-manager")
public class TicketCategoryController {

    private final TicketCategoryServices ticketCategoryServices;

    @PostMapping("/ticket-categories")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_CATEGORY_MANAGER')")
    public ResponseEntity<PageItemTicketCategoryDto> createTicketCategory(@RequestBody @Validated CreateTicketCategoryDto ticketCategoryDto, JwtAuthenticationToken token, UriComponentsBuilder uriBuilder){
        var ticketCategory = ticketCategoryServices.save(ticketCategoryDto, token);
        var uri = uriBuilder.path("/ticket-categories/{id}").buildAndExpand(ticketCategory.getTicketCategoryId()).toUri();
        return ResponseEntity.created(uri).body(new PageItemTicketCategoryDto(ticketCategory));
        
    }

    @PutMapping("/ticket-categories/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_CATEGORY_MANAGER')")
    public ResponseEntity<Void> updateTicketCategory(@RequestBody @Validated TicketCategoryDto ticketCategoryDto, JwtAuthenticationToken token){
        ticketCategoryServices.update(ticketCategoryDto, token);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/ticket-categories/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_CATEGORY_MANAGER')")
    public ResponseEntity<Void> deleteTicketCategory(@PathVariable("id") String ticketCategoryId, JwtAuthenticationToken token){
        ticketCategoryServices.delete(ticketCategoryId, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("ticket-categories/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_CATEGORY')")
    public ResponseEntity<TicketCategory> findById(@PathVariable("id") String ticketCategoryId, JwtAuthenticationToken token){
        var ticketCategory = ticketCategoryServices.findById(ticketCategoryId, token);
        return ResponseEntity.ok(ticketCategory);
    }

    @GetMapping("/ticket-categories")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_CATEGORY')")
    public ResponseEntity<Page<PageItemTicketCategoryDto>> findAll(Pageable pageable, JwtAuthenticationToken token){
        var ticketCategories = ticketCategoryServices.findAll(pageable);
        return ResponseEntity.ok(ticketCategories);
    }
    
    @PatchMapping("/ticket-categories/{id}/activate")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_CATEGORY_MANAGER')")
    public ResponseEntity<Void> activateTicketCategory(@PathVariable("id") String ticketCategoryId, JwtAuthenticationToken token){
        ticketCategoryServices.activate(ticketCategoryId, token);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/ticket-categories/{id}/deactivate")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_CATEGORY_MANAGER')")
    public ResponseEntity<Void> deactivateTicketCategory(@PathVariable("id") String ticketCategoryId, JwtAuthenticationToken token){
        ticketCategoryServices.deactivate(ticketCategoryId, token);
        return ResponseEntity.ok().build();
    }
}
