package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketLogDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemTicketLogDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketLog;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketLogRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.UserRepository;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketLogServices;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketLogServiceImpl implements TicketLogServices{
    
    private final UserRepository userRep;
    private final TicketLogRepository ticketLogRep;
    private final TicketRepository ticketRep;

    @Override
    @Transactional
    public Void save(@RequestBody @Valid CreateTicketLogDto ticketLogDto, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var ticket = ticketRep.findById(ticketLogDto.ticketId()).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        try {
            var ticketLog = new TicketLog();
            ticketLog.setTicket(ticket);
            ticketLog.setUser(user);
            ticketLog.setLogDescription(ticketLogDto.logDescription());
            ticketLogRep.save(ticketLog);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }    

        return null;
    }

    @Override
    @Transactional
    public TicketLog findById(@RequestParam String ticketLogId, JwtAuthenticationToken token){
        
        var ticketLog = ticketLogRep.findById(UUID.fromString(ticketLogId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        return ticketLog;
    }

    @Override
    @Transactional
    public Void delete(@RequestParam String ticketLogId, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        if(!user.isAdmin()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        var ticketLog = ticketLogRep.findById(UUID.fromString(ticketLogId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        try {
            ticketLogRep.delete(ticketLog);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return null;
    }

    @Override
    @Transactional
    public Page<PageItemTicketLogDto> findAll(Pageable pageable){;
        
        var tickets = ticketLogRep.findAll(pageable).map(PageItemTicketLogDto::new);
        return tickets;

    }

    @Override
    @Transactional
    public Page<PageItemTicketLogDto> findTicketsLogByTicket(@RequestParam String ticketId, Pageable pageable){
        
        var ticket = ticketRep.findById(UUID.fromString(ticketId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var ticketLogs = ticketLogRep.findByTicket(ticket, pageable).map(PageItemTicketLogDto::new);
        return ticketLogs;

    }

    @Override
    @Transactional
    public Page<PageItemTicketLogDto> findTicketLogsByUser(@RequestParam String userId, Pageable pageable){

        var user = userRep.findById(UUID.fromString(userId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var ticketLogs = ticketLogRep.findByUser(user, pageable).map(PageItemTicketLogDto::new);
        return ticketLogs;

    }
}
