package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.List;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketTypeDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketType;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketTypeServices;
import jakarta.transaction.Transactional;

public class TicketTypeServiceImpl implements TicketTypeServices{

    @Override
    @Transactional
    public TicketType save(CreateTicketTypeDto ticketType, JwtAuthenticationToken token){
        // TODO
        return null;
    }

    @Override
    @Transactional
    public TicketType findById(String ticketTypeId, JwtAuthenticationToken token){
        // TODO
        return null;
    }

    @Override
    @Transactional
    public Void delete(String ticketTypeId, JwtAuthenticationToken token){
        // TODO
        return null;
    }

    @Override
    @Transactional
    public List<TicketType> findAll(JwtAuthenticationToken token){
        // TODO
        return null;
    }

    @Override
    @Transactional
    public List<TicketType> findTicketsByTicketTypeId(String ticketTypeId, JwtAuthenticationToken token){
        // TODO
        return null;
    }

    @Override
    @Transactional
    public TicketType update(CreateTicketTypeDto ticketType, JwtAuthenticationToken token){
        // TODO
        return null;
    }

}
