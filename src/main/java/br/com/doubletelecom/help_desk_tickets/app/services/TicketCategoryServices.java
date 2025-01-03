/**
 * Service interface for managing Ticket Categorys.
 * Provides methods for creating, retrieving, updating, and deleting ticket Categorys.
 * Also includes methods for retrieving all ticket Categorys.
 * 
 * Methods:
 * - save(CreateTicketCategoryDto ticketCategoryDto, JwtAuthenticationToken token): Saves a new ticket Category.
 * - findById(String ticketCategoryId, JwtAuthenticationToken token): Finds a ticket Category by its ID.
 * - delete(String ticketCategoryId, JwtAuthenticationToken token): Deletes a ticket Category by its ID.
 * - findAll(): Retrieves all ticket Categorys.
 * - update(TicketCategoryDto ticketCategoryDto, JwtAuthenticationToken token): Updates an existing ticket Category.
 */
package br.com.doubletelecom.help_desk_tickets.app.services;

import java.util.List;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketCategoryDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.TicketCategoryDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TickeCategory;

public interface TicketCategoryServices {
    public TickeCategory save(CreateTicketCategoryDto ticketCategoryDto, JwtAuthenticationToken token);
    public TickeCategory findById(String ticketCategoryId, JwtAuthenticationToken token);
    public Void delete(String ticketCategoryId, JwtAuthenticationToken token);
    public List<TickeCategory> findAll();
    public TickeCategory update(TicketCategoryDto ticketCategoryDto, JwtAuthenticationToken token);
    public Void activate(String ticketCategoryId, JwtAuthenticationToken token);
    public Void deactivate(String ticketCategoryId, JwtAuthenticationToken token);
}