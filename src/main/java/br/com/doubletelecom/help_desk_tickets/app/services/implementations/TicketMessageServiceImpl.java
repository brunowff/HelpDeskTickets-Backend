package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.List;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketMessageDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketMessage;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketMessageServices;
import jakarta.transaction.Transactional;

public class TicketMessageServiceImpl implements TicketMessageServices{
    
    @Override
    @Transactional
    public TicketMessage save(CreateTicketMessageDto ticketMessage, JwtAuthenticationToken token){
        // TODO
        return null;
    }

    @Override
    @Transactional
    public TicketMessage findById(String ticketMessageId, JwtAuthenticationToken token){
        // TODO
        return null;
    }
    
    @Override
    @Transactional
    public Void delete(String ticketMessageId, JwtAuthenticationToken token){
        // TODO
        return null;
    }

    @Override
    @Transactional
    public List<TicketMessage> findAll(JwtAuthenticationToken token){
        // TODO
        return null;
    }

    @Override
    @Transactional
    public List<TicketMessage> findTicketMessagesByTicketId(String ticketId, JwtAuthenticationToken token){
        // TODO
        return null;
    }

    @Override
    @Transactional
    public List<TicketMessage> findTicketMessagesByUserId(String userId, JwtAuthenticationToken token){
        // TODO
        return null;
    }
}

