package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketMessageDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketMessage;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketMessageRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.UserRepository;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketMessageServices;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketMessageServiceImpl implements TicketMessageServices{
    
    private final TicketRepository ticketRep;
    private final TicketMessageRepository ticketMessageRep;
    private final UserRepository userRep;
    
    @Override
    @Transactional
    public TicketMessage save(@RequestBody @Valid CreateTicketMessageDto ticketMessageDto, JwtAuthenticationToken token){
        
        var ticket = ticketRep.findById(ticketMessageDto.ticketId()).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        try {
            var ticketMessage = new TicketMessage();
            ticketMessage.setTicket(ticket);
            ticketMessage.setUser(user);
            ticketMessage.setMessage(ticketMessageDto.message());
            ticketMessageRep.save(ticketMessage);
            return ticketMessage;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        
    }

    @Override
    @Transactional
    public TicketMessage findById(@RequestParam String ticketMessageId, JwtAuthenticationToken token){
        
        var ticketMessage = ticketMessageRep.findById(UUID.fromString(ticketMessageId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ticketMessage;
    }
    
    @Override
    @Transactional
    public Void delete(@RequestParam String ticketMessageId, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(!user.isAdmin()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        var ticketMessage = ticketMessageRep.findById(UUID.fromString(ticketMessageId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        try {
            ticketMessageRep.delete(ticketMessage);
        } catch ( Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        
        return null;
    }

    @Override
    @Transactional
    public Page<TicketMessage> findAll(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int pageSize){
        
        var ticketMessages = ticketMessageRep.findAll(PageRequest.of(page, pageSize, Sort.Direction.DESC, "creationTimestamp"));

        return ticketMessages;
    }

    @Override
    @Transactional
    public List<TicketMessage> findTicketMessagesByTicketId(@RequestParam String ticketId, JwtAuthenticationToken token){
        
        var ticket = ticketRep.findById(UUID.fromString(ticketId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var ticketMessages = ticketMessageRep.findByTicket(ticket);
        return ticketMessages;
    }

    @Override
    @Transactional
    public List<TicketMessage> findTicketMessagesByUserId(@RequestParam String userId, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(userId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var ticketMessages = ticketMessageRep.findByUser(user);
        return ticketMessages;
    }
}

