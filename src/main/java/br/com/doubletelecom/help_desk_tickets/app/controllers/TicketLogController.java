/**
 * Controller for managing Ticket Logs.
 * Provides endpoints for creating, deleting, finding, and listing ticket logs.
 * 
 * Endpoints:
 * - POST /ticketlog: Create a new ticket log.
 * - DELETE /ticketlog/{id}: Delete a ticket log by ID.
 * - GET /ticketlog/{id}: Find a ticket log by ID.
 * - GET /ticketlogs: List all ticket logs with pagination.
 * 
 * Security:
 * - Requires appropriate OAuth2 scopes for each endpoint.
 * 
 * Dependencies:
 * - TicketLogServices: Service layer for ticket log operations.
 * 
 * Annotations:
 * - @RestController: Marks this class as a Spring MVC controller.
 * - @AllArgsConstructor: Generates a constructor with 1 parameter for each field in the class.
 * - @PreAuthorize: Specifies security constraints on each endpoint.
 * 
 * @author 
 * @version
 */

package br.com.doubletelecom.help_desk_tickets.app.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketLogDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemTicketLogDto;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketLogServices;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@AllArgsConstructor
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/ticket-log-manager")
public class TicketLogController {

    private final TicketLogServices ticketLogServices;

    @PostMapping("/ticketlog")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_LOG_MANAGER')")
    public ResponseEntity<PageItemTicketLogDto> createTicketLog(@RequestBody @Validated CreateTicketLogDto ticketLogDto, JwtAuthenticationToken token, UriComponentsBuilder uriBuilder){
        var ticketLog = ticketLogServices.save(ticketLogDto, token);
        var uri = uriBuilder.path("/ticketlog/{id}").buildAndExpand(ticketLog).toUri();
        return ResponseEntity.created(uri).body(new PageItemTicketLogDto(ticketLog));
    }

    @DeleteMapping("/ticketlog/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_LOG_MANAGER')")
    public ResponseEntity<Void> deleteTicketLog(@PathVariable("id") String ticketLogId, JwtAuthenticationToken token){
        ticketLogServices.delete(ticketLogId, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("ticketlog/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_BASIC') or hasAuthority('SCOPE_API_LOG')")
    public ResponseEntity<PageItemTicketLogDto> findById(@PathVariable("id") String ticketLogId, JwtAuthenticationToken token){
        var ticketLog = ticketLogServices.findById(ticketLogId, token);
        return ResponseEntity.ok(new PageItemTicketLogDto(ticketLog));
    }
    
    @GetMapping("/ticketlogs")
    @PreAuthorize("hasAuthority('SCOPE_API_BASIC') or hasAuthority('SCOPE_API_LOG')")
    public ResponseEntity<Page<PageItemTicketLogDto>> findAll(@PageableDefault(page = 0, size = 20) Pageable pageable, JwtAuthenticationToken token){
        var ticketLogs = ticketLogServices.findAll(pageable);
        return ResponseEntity.ok(ticketLogs);
    }

    @GetMapping("/ticketlogs/{id}/ticket")
    @PreAuthorize("hasAuthority('SCOPE_API_BASIC') or hasAuthority('SCOPE_API_LOG')")
    public ResponseEntity<Page<PageItemTicketLogDto>> findByTicketId(@PathVariable("id") String ticketId, JwtAuthenticationToken token, Pageable pageable){
        var ticketLogs = ticketLogServices.findTicketsLogByTicketId(ticketId, pageable);
        return ResponseEntity.ok(ticketLogs);
    }

    @GetMapping("/ticketlogs/{id}/user")
    @PreAuthorize("hasAuthority('SCOPE_API_BASIC') or hasAuthority('SCOPE_API_LOG')")
    public ResponseEntity<Page<PageItemTicketLogDto>> findByUserId(@PathVariable("id") String userId, JwtAuthenticationToken token, Pageable pageable){
        var ticketLogs = ticketLogServices.findTicketLogsByUserId(userId, pageable);
        return ResponseEntity.ok(ticketLogs);
    }
    
    
}
