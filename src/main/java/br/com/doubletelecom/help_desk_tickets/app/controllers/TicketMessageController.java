/**
 * REST controller para gerenciamento de mensagens de tickets.
 *
 * <p>Correções aplicadas:
 * <ul>
 *   <li>findById: trocado {@code @RequestParam} por {@code @PathVariable} — o ID vem no path /{id}.</li>
 *   <li>findByTicketId / findByUserId: idem — IDs vêm no path, não como query param.</li>
 *   <li>findById: retorna {@link PageItemTicketMessageDto} em vez da entidade bruta (evita vazar senha).</li>
 *   <li>deleteTicketMessage: método HTTP trocado de PUT para DELETE — semântica correta para remoção.</li>
 *   <li>findAll / findByTicketId / findByUserId: exceções propagadas ao {@code ExceptionHandlerAdvice}
 *       em vez de retornar 500 silencioso.</li>
 * </ul>
 */
package br.com.doubletelecom.help_desk_tickets.app.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketMessageDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemTicketMessageDto;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketMessageServices;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@AllArgsConstructor
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/ticket-message-manager")
public class TicketMessageController {

    private final TicketMessageServices ticketMessageServices;

    @PostMapping("/ticket-message")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_BASIC') or hasAuthority('SCOPE_API_TICKET_MESSAGE')")
    public ResponseEntity<PageItemTicketMessageDto> createTicketMessage(
            @RequestBody @Validated CreateTicketMessageDto ticketMessageDto,
            JwtAuthenticationToken token,
            UriComponentsBuilder uriBuilder) {
        var ticketMessage = ticketMessageServices.save(ticketMessageDto, token);
        var uri = uriBuilder.path("/ticket-message-manager/ticket-message/{id}")
                .buildAndExpand(ticketMessage.getTicketMessageId()).toUri();
        return ResponseEntity.created(uri).body(new PageItemTicketMessageDto(ticketMessage));
    }

    // BUG FIX: era PUT — DELETE é o verbo correto para remoção
    @DeleteMapping("/ticket-message/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_MESSAGE_MANAGER')")
    public ResponseEntity<Void> deleteTicketMessage(
            @PathVariable("id") String ticketMessageId,
            JwtAuthenticationToken token) {
        ticketMessageServices.delete(ticketMessageId, token);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/ticket-messages")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_TICKET_MESSAGE_MANAGER') or hasAuthority('SCOPE_API_BASIC') or hasAuthority('SCOPE_API_TICKET_MESSAGE')")
    public ResponseEntity<Page<PageItemTicketMessageDto>> findAll(
            @PageableDefault(page = 0, size = 20) Pageable pageable,
            JwtAuthenticationToken token) {
        // BUG FIX: exceções propagadas ao ExceptionHandlerAdvice em vez de retornar 500 silencioso
        var ticketMessages = ticketMessageServices.findAll(pageable, token);
        return ResponseEntity.ok(ticketMessages);
    }

    // BUG FIX: era @RequestParam — o ID vem no path /{id}, não como query param
    // BUG FIX: retorna PageItemTicketMessageDto em vez da entidade bruta (evita vazar senha)
    @GetMapping("/ticket-message/{id}")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_BASIC') or hasAuthority('SCOPE_API_TICKET_MESSAGE') or hasAuthority('SCOPE_API_TICKET_MESSAGE_MANAGER')")
    public ResponseEntity<PageItemTicketMessageDto> findById(
            @PathVariable("id") String ticketMessageId,
            JwtAuthenticationToken token) {
        var ticketMessage = ticketMessageServices.findById(ticketMessageId, token);
        return ResponseEntity.ok(new PageItemTicketMessageDto(ticketMessage));
    }

    // BUG FIX: era @RequestParam ticketId — o ID vem no path /{id}
    @GetMapping("/ticket-message/{id}/ticket")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_BASIC') or hasAuthority('SCOPE_API_TICKET_MESSAGE') or hasAuthority('SCOPE_API_TICKET_MESSAGE_MANAGER')")
    public ResponseEntity<Page<PageItemTicketMessageDto>> findByTicketId(
            @PathVariable("id") String ticketId,
            Pageable pageable,
            JwtAuthenticationToken token) {
        var ticketMessages = ticketMessageServices.findTicketMessagesByTicketId(ticketId, pageable, token);
        return ResponseEntity.ok(ticketMessages);
    }

    // BUG FIX: era @RequestParam userId — o ID vem no path /{id}
    @GetMapping("/ticket-message/{id}/user")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN') or hasAuthority('SCOPE_API_BASIC') or hasAuthority('SCOPE_API_TICKET_MESSAGE') or hasAuthority('SCOPE_API_TICKET_MESSAGE_MANAGER')")
    public ResponseEntity<Page<PageItemTicketMessageDto>> findByUserId(
            @PathVariable("id") String userId,
            Pageable pageable,
            JwtAuthenticationToken token) {
        var ticketMessages = ticketMessageServices.findTicketMessagesByUserId(userId, pageable, token);
        return ResponseEntity.ok(ticketMessages);
    }
}
