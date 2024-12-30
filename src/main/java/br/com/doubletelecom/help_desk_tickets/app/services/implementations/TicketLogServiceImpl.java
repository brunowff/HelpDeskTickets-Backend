package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.List;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Ticket;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketLogServices;
import jakarta.transaction.Transactional;

public class TicketLogServiceImpl implements TicketLogServices{
    
    @Override
    @Transactional
    public TicketLogServices save(TicketLogServices ticketLogServices, JwtAuthenticationToken token){
        // TODO
        return null;
    }

    @Override
    @Transactional
    public TicketLogServices findById(String ticketLogServicesId, JwtAuthenticationToken token){
        // TODO
        return null;
    }

    @Override
    @Transactional
    public Void delete(String ticketLogServicesId, JwtAuthenticationToken token){
        // TODO
        return null;
    }

    @Override
    @Transactional
    public List<TicketLogServices> findAll(JwtAuthenticationToken token){;
        // TODO
        return null;
    }

    @Override
    @Transactional
    public List<Ticket> findTicketsLogByTicketId(String ticketId, JwtAuthenticationToken token){
        // TODO
        return null;
    }

    @Override
    @Transactional
    public List<TicketLogServices> findTicketLogsByUserId(String userId, JwtAuthenticationToken token){
        // TODO
        return null;
    }
}
