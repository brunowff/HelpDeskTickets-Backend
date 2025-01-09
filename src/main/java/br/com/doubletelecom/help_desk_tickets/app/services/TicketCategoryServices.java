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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketCategoryDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemTicketCategoryDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.TicketCategoryDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketCategory;

public interface TicketCategoryServices {
    public TicketCategory save(CreateTicketCategoryDto ticketCategoryDto, JwtAuthenticationToken token);
    public TicketCategory findById(String ticketCategoryId, JwtAuthenticationToken token);
    public Void delete(String ticketCategoryId, JwtAuthenticationToken token);
    public Page<PageItemTicketCategoryDto> findAll(Pageable pageable);
    public TicketCategory update(TicketCategoryDto ticketCategoryDto, JwtAuthenticationToken token);
    public Void activate(String ticketCategoryId, JwtAuthenticationToken token);
    public Void deactivate(String ticketCategoryId, JwtAuthenticationToken token);
}