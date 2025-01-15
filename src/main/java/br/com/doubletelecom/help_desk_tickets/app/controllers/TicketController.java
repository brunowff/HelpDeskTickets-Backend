/**
 * TicketController is a REST controller that manages ticket-related operations.
 * It provides endpoints for creating, updating, deleting, and retrieving tickets.
 * 
 * Endpoints:
 * 
 * - POST /tickets: Creates a new ticket.
 * - GET /dashboard: Retrieves a paginated list of tickets for the dashboard.
 * - DELETE /tickets/{id}: Deletes a ticket by its ID.
 * - PUT /tickets/{id}: Updates a ticket by its ID.
 * - PATCH /tickets/{id}/status: Updates the status of a ticket by its ID.
 * - PATCH /tickets/{id}/priority: Updates the priority of a ticket by its ID.
 * - PATCH /tickets/{id}/attribuitedTo: Updates the user to whom the ticket is attributed by its ID.
 * - PATCH /tickets/{id}/ticketCategory: Updates the category of a ticket by its ID.
 * - GET /tickets/{id}/userId: Retrieves a paginated list of tickets by user ID.
 * - GET /tickets/{id}/attribuitedTo: Retrieves a paginated list of tickets attributed to a user by user ID.
 * - GET /tickets/{id}/ticketCategoryId: Retrieves a paginated list of tickets by category ID.
 * - GET /tickets/{status}/status: Retrieves a paginated list of tickets by status.
 * - GET /tickets/{priority}/priority: Retrieves a paginated list of tickets by priority.
 * - GET /tickets/{title}/title: Retrieves a paginated list of tickets by title.
 * - GET /tickets/{description}/description: Retrieves a paginated list of tickets by description.
 * 
 * All endpoints require a JwtAuthenticationToken for authentication and authorization.
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemTicketDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.TicketDto;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.business.ObjectNotFoundException;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketServices;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/ticket-manager")
@AllArgsConstructor
public class TicketController {
    
    private final TicketServices ticketServices;

    @PostMapping("/tickets")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_CATEGORY_MANAGER')")
    public ResponseEntity<PageItemTicketDto> createTicket(@RequestBody @Validated CreateTicketDto ticketDto, JwtAuthenticationToken token, UriComponentsBuilder uriBuilder){
        var ticket = ticketServices.save(ticketDto, token);
        var uri = uriBuilder.path("/tickets/{id}").buildAndExpand(ticket.getTicketId()).toUri();
        return ResponseEntity.created(uri).body(new PageItemTicketDto(ticket));
        
    }

    @GetMapping("/tickets/dashboard")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_MANAGER') or hasAuthority('SCOPE_API_TICKET')")
    public ResponseEntity<Page<PageItemTicketDto>> dashboard(Pageable pageable){
        try {
            var tickets = ticketServices.dashboard(pageable);
            return ResponseEntity.ok(tickets);

        } catch (Exception e) {
            throw new ObjectNotFoundException();
        }
    }

    @DeleteMapping("/tickets/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_MANAGER') or hasAuthority('SCOPE_API_TICKET')")
    public ResponseEntity<Void> deleteTicket(@PathVariable("id") String ticketId, JwtAuthenticationToken token){
        ticketServices.deleteTicket(ticketId, token);
        return ResponseEntity.ok().build();

    }

    @PutMapping("tickets/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_MANAGER') or hasAuthority('SCOPE_API_TICKET')")
    public ResponseEntity<Void> updateTicket(@PathVariable("id") String id, @RequestBody @Validated TicketDto ticketDto, JwtAuthenticationToken token){
        ticketServices.update(ticketDto, token);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("tickets/{id}/status")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_MANAGER') or hasAuthority('SCOPE_API_TICKET')")
    public ResponseEntity<Void> updateStatus(@PathVariable("id") String id, @RequestParam("status") String status, JwtAuthenticationToken token){
        ticketServices.updateStatus(id, status, token);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("tickets/{id}/priority")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_MANAGER') or hasAuthority('SCOPE_API_TICKET')")
    public ResponseEntity<Void> updatePriority(@PathVariable("id") String id, @RequestParam("priority") String priority, JwtAuthenticationToken token){
        ticketServices.updatePriority(id, priority, token);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("tickets/{id}/attribuitedTo")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_MANAGER') or hasAuthority('SCOPE_API_TICKET')")
    public ResponseEntity<Void> updateAttribuitedTo(@PathVariable("id") String id, @RequestParam("userId") String userId, JwtAuthenticationToken token){
        ticketServices.updateAttribuitedTo(id, userId, token);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("tickets/{id}/ticketCategory")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_MANAGER') or hasAuthority('SCOPE_API_TICKET')")
    public ResponseEntity<Void> updateTicketCategory(@PathVariable("id") String id, @RequestParam("ticketCategoryId") String ticketCategoryId, JwtAuthenticationToken token){
        ticketServices.updateTicketCategory(id, ticketCategoryId, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("tickets/{id}/userId")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_MANAGER') or hasAuthority('SCOPE_API_TICKET')")
    public ResponseEntity<Page<PageItemTicketDto>> findTicketsByUserId(@PathVariable("id") String userId, Pageable pageable){
        var tickets = ticketServices.findTicketsByUserId(userId, pageable);
        return ResponseEntity.ok(tickets);
    }
    
    @GetMapping("tickets/{id}/attribuitedTo")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_MANAGER') or hasAuthority('SCOPE_API_TICKET')")
    public ResponseEntity<Page<PageItemTicketDto>> findTicketsByAttribuitedToUser(@PathVariable("id") String userId, Pageable pageable){
        var tickets = ticketServices.findTicketsByAttribuitedToUser(userId, pageable);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("tickets/{id}/ticketCategoryId")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_MANAGER') or hasAuthority('SCOPE_API_TICKET')")
    public ResponseEntity<Page<PageItemTicketDto>> findTicketsByTicketCategoryId(@PathVariable("id") String ticketCategoryId, Pageable pageable){
        var tickets = ticketServices.findTicketsByTicketCategory(ticketCategoryId, pageable);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("tickets/{status}/status")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_MANAGER') or hasAuthority('SCOPE_API_TICKET')")
    public ResponseEntity<Page<PageItemTicketDto>> findTicketsByStatus(@PathVariable("status") String status, Pageable pageable){
        var tickets = ticketServices.findTicketsByStatus(status, pageable);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("tickets/{priority}/priority")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_MANAGER') or hasAuthority('SCOPE_API_TICKET')")
    public ResponseEntity<Page<PageItemTicketDto>> findTicketsByPriority(@PathVariable("priority") String priority, Pageable pageable){
        var tickets = ticketServices.findTicketsByPriority(priority, pageable);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("tickets/{title}/title")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_MANAGER') or hasAuthority('SCOPE_API_TICKET')")
    public ResponseEntity<Page<PageItemTicketDto>> findTicketsByTitle(@PathVariable("title") String title, Pageable pageable){
        var tickets = ticketServices.findTicketsByTitle(title, pageable);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("tickets/{description}/description")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_MANAGER') or hasAuthority('SCOPE_API_TICKET')")
    public ResponseEntity<Page<PageItemTicketDto>> findTicketsByDescription(@PathVariable("description") String description, Pageable pageable){
        var tickets = ticketServices.findTicketsByDescription(description, pageable);
        return ResponseEntity.ok(tickets);
    }
    

}
