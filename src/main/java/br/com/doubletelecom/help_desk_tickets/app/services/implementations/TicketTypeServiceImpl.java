package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketTypeDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketType;
import br.com.doubletelecom.help_desk_tickets.app.repositories.GroupRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketTypeRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.UserRepository;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketTypeServices;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketTypeServiceImpl implements TicketTypeServices{

    private final UserRepository userRep;
    private final GroupRepository groupRep;
    private final TicketTypeRepository ticketTypeRep;

    @Override
    @Transactional
    public TicketType save(@RequestBody @Valid CreateTicketTypeDto ticketTypeDto, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        if(!user.isAdmin()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        var group = groupRep.findById(ticketTypeDto.destinationGroup().getGroupId()).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var ticketType = new TicketType();

        try {
            ticketType.setDestinationGroup(group);
            ticketType.setName(ticketTypeDto.name());
            ticketTypeRep.save(ticketType);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return ticketType;
    }

    @Override
    @Transactional
    public TicketType findById(@RequestParam String ticketTypeId, JwtAuthenticationToken token){
        
        var ticketType = ticketTypeRep.findById(UUID.fromString(ticketTypeId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ticketType;
    }

    @Override
    @Transactional
    public Void delete(@RequestParam String ticketTypeId, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(!user.isAdmin()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        try {
            var ticketType = ticketTypeRep.findById(UUID.fromString(ticketTypeId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            ticketTypeRep.delete(ticketType);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return null;
    }

    @Override
    @Transactional
    public List<TicketType> findAll(){
        
        var ticketTypes = ticketTypeRep.findAll();
        return ticketTypes;
    }

    @Override
    @Transactional
    public TicketType update(@RequestBody @Valid CreateTicketTypeDto ticketTypeDto, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(!user.isAdmin()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        var ticketType = new TicketType();

        try {
            var group = groupRep.findById(ticketTypeDto.destinationGroup().getGroupId()).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            ticketType.setName(ticketTypeDto.name());
            ticketType.setDestinationGroup(group);
            ticketTypeRep.save(ticketType);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return ticketType;
    }

}
