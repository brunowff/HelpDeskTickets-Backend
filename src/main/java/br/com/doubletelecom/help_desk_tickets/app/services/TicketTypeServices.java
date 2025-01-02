/**
 * Service interface for managing Ticket Types.
 * Provides methods for creating, retrieving, updating, and deleting ticket types.
 * Also includes methods for retrieving all ticket types.
 * 
 * Methods:
 * - save(CreateTicketTypeDto ticketTypeDto, JwtAuthenticationToken token): Saves a new ticket type.
 * - findById(String ticketTypeId, JwtAuthenticationToken token): Finds a ticket type by its ID.
 * - delete(String ticketTypeId, JwtAuthenticationToken token): Deletes a ticket type by its ID.
 * - findAll(): Retrieves all ticket types.
 * - update(TicketTypeDto ticketTypeDto, JwtAuthenticationToken token): Updates an existing ticket type.
 */
package br.com.doubletelecom.help_desk_tickets.app.services;

import java.util.List;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketTypeDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.TicketTypeDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketType;

public interface TicketTypeServices {
    public TicketType save(CreateTicketTypeDto ticketTypeDto, JwtAuthenticationToken token);
    public TicketType findById(String ticketTypeId, JwtAuthenticationToken token);
    public Void delete(String ticketTypeId, JwtAuthenticationToken token);
    public List<TicketType> findAll();
    public TicketType update(TicketTypeDto ticketTypeDto, JwtAuthenticationToken token);
    public Void activate(String ticketTypeId, JwtAuthenticationToken token);
    public Void deactivate(String ticketTypeId, JwtAuthenticationToken token);
}