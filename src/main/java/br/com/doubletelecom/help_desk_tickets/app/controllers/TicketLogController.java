package br.com.doubletelecom.help_desk_tickets.app.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketLogDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemTicketLogDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketLog;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketLogServices;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController("/tlm")
@AllArgsConstructor
public class TicketLogController {

    private final TicketLogServices ticketLogServices;

    @PostMapping("/ticketlog")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_LOG_MANAGER')")
    public ResponseEntity<Void> createTicketLog(@RequestBody CreateTicketLogDto ticketLogDto, JwtAuthenticationToken token){
        ticketLogServices.save(ticketLogDto, token);
        return ResponseEntity.ok().build();
        
    }

    @DeleteMapping("/ticketlog/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_LOG_MANAGER')")
    public ResponseEntity<Void> deleteTicketLog(@PathVariable("id") String ticketLogId, JwtAuthenticationToken token){
        ticketLogServices.delete(ticketLogId, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("ticketlog/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_BASIC') or hasAuthority('SCOPE_API_LOG')")
    public ResponseEntity<TicketLog> findById(@RequestParam String ticketLogId, JwtAuthenticationToken token){
        var ticktLog = ticketLogServices.findById(ticketLogId, token);
        return ResponseEntity.ok(ticktLog);
    }
    
    @GetMapping("/ticketlogs")
    @PreAuthorize("hasAuthority('SCOPE_API_BASIC') or hasAuthority('SCOPE_API_LOG')")
    public ResponseEntity<Page<PageItemTicketLogDto>> findAll(@PageableDefault(page = 0, size = 20) Pageable pageable, JwtAuthenticationToken token) {
        
        try {
            var ticketLogs = ticketLogServices.findAll(pageable);
            return ResponseEntity.ok(ticketLogs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
  
    }

}
