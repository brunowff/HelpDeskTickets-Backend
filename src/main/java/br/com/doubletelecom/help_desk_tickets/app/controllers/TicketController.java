package br.com.doubletelecom.help_desk_tickets.app.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemTicketDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.TicketDto;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketServices;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController("/tm")
@AllArgsConstructor
public class TicketController {
    
    private final TicketServices ticketServices;

    @PostMapping("/tickets")
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

    @PutMapping("tickets/{id}")
    public ResponseEntity<Void> updateTicket(@PathVariable("id") String id, @RequestBody TicketDto ticketDto, JwtAuthenticationToken token){
        ticketServices.update(ticketDto, token);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("tickets/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable("id") String id, @RequestParam("status") String status, JwtAuthenticationToken token){
        ticketServices.updateStatus(id, status, token);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("tickets/{id}/priority")
    public ResponseEntity<Void> updatePriority(@PathVariable("id") String id, @RequestParam("priority") String priority, JwtAuthenticationToken token){
        ticketServices.updatePriority(id, priority, token);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("tickets/{id}/attribuitedTo")
    public ResponseEntity<Void> updateAttribuitedTo(@PathVariable("id") String id, @RequestParam("userId") String userId, JwtAuthenticationToken token){
        ticketServices.updateAttribuitedTo(id, userId, token);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("tickets/{id}/ticketCategory")
    public ResponseEntity<Void> updateTicketCategory(@PathVariable("id") String id, @RequestParam("ticketCategoryId") String ticketCategoryId, JwtAuthenticationToken token){
        ticketServices.updateTicketCategory(id, ticketCategoryId, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("tickets/{id}/userId")
    public ResponseEntity<Page<PageItemTicketDto>> findTicketsByUserId(@PathVariable("id") String userId, Pageable pageable){
        var tickets = ticketServices.findTicketsByUserId(userId, pageable);
        return ResponseEntity.ok(tickets);
    }
    
    @GetMapping("tickets/{id}/attribuitedTo")
    public ResponseEntity<Page<PageItemTicketDto>> findTicketsByAttribuitedToUser(@PathVariable("id") String userId, Pageable pageable){
        var tickets = ticketServices.findTicketsByAttribuitedToUser(userId, pageable);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("tickets/{id}/ticketCategoryId")
    public ResponseEntity<Page<PageItemTicketDto>> findTicketsByTicketCategoryId(@PathVariable("id") String ticketCategoryId, Pageable pageable){
        var tickets = ticketServices.findTicketsByTicketCategoryId(ticketCategoryId, pageable);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("tickets/{status}/status")
    public ResponseEntity<Page<PageItemTicketDto>> findTicketsByStatus(@PathVariable("status") String status, Pageable pageable){
        var tickets = ticketServices.findTicketsByStatus(status, pageable);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("tickets/{priority}/priority")
    public ResponseEntity<Page<PageItemTicketDto>> findTicketsByPriority(@PathVariable("priority") String priority, Pageable pageable){
        var tickets = ticketServices.findTicketsByPriority(priority, pageable);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("tickets/{title}/title")
    public ResponseEntity<Page<PageItemTicketDto>> findTicketsByTitle(@PathVariable("title") String title, Pageable pageable){
        var tickets = ticketServices.findTicketsByTitle(title, pageable);
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("tickets/{description}/description")
    public ResponseEntity<Page<PageItemTicketDto>> findTicketsByDescription(@PathVariable("description") String description, Pageable pageable){
        var tickets = ticketServices.findTicketsByDescription(description, pageable);
        return ResponseEntity.ok(tickets);
    }
    

}
