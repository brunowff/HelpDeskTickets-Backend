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

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateTicketCategoryDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemTicketCategoryDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.TicketCategoryDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.TicketCategory;
import br.com.doubletelecom.help_desk_tickets.app.repositories.GroupRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.TicketCategoryRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.UserRepository;
import br.com.doubletelecom.help_desk_tickets.app.services.TicketCategoryServices;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TicketCategoryServiceImpl implements TicketCategoryServices{

    private final UserRepository userRep;
    private final GroupRepository groupRep;
    private final TicketCategoryRepository ticketCategoryRep;

    @Override
    @Transactional
    public TicketCategory save(@RequestBody @Valid CreateTicketCategoryDto ticketCategoryDto, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        if(user.isAdmin() || user.hasRole("API_TICKET_CATEGORY_MANAGER")){
            
            var group = groupRep.findById(ticketCategoryDto.destinationGroup().getGroupId()).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            
            if(!group.getActive()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Group is not active");
            }

            var ticketCategory = new TicketCategory();
    
            try {
                ticketCategory.setDestinationGroup(group);
                ticketCategory.setName(ticketCategoryDto.name());
                ticketCategory.setActive(true);
                ticketCategoryRep.save(ticketCategory);
                return ticketCategory;
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
 
    }

    @Override
    @Transactional
    public TicketCategory findById(@RequestParam String ticketCategoryId, JwtAuthenticationToken token){
        
        var ticketCategory = ticketCategoryRep.findById(UUID.fromString(ticketCategoryId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return ticketCategory;
    }

    @Override
    @Transactional
    public Void delete(@RequestParam String ticketCategoryId, JwtAuthenticationToken token){

        // It not recomended to delete a TicketCategory, but just inactivate it.
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(!user.isAdmin()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        try {
            var ticketCategory = ticketCategoryRep.findById(UUID.fromString(ticketCategoryId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            ticketCategoryRep.delete(ticketCategory);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return null;
    }

    @Override
    @Transactional
    public Page<PageItemTicketCategoryDto> findAll(Pageable pageable){
        
        var ticketCategories = ticketCategoryRep.findAll(pageable).map(PageItemTicketCategoryDto::new);
        return ticketCategories;
    }

    @Override
    @Transactional
    public TicketCategory update(@RequestBody @Valid TicketCategoryDto ticketCategoryDto, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(user.isAdmin() || user.hasRole("API_TICKET_CATEGORY_MANAGER")){
            try {
                var ticketCategory = new TicketCategory();
                var group = groupRep.findById(ticketCategoryDto.destinationGroup().getGroupId()).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
                ticketCategory.setName(ticketCategoryDto.name());
                ticketCategory.setDestinationGroup(group);
                ticketCategory.setActive(ticketCategoryDto.active());
                ticketCategoryRep.save(ticketCategory);
                return ticketCategory;
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        
    }

    @Override
    @Transactional
    public Void activate(@RequestParam String ticketCategoryId, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(user.isAdmin() || user.hasRole("API_TICKET_CATEGORY_MANAGER")){
            try {
                var ticketCategory = ticketCategoryRep.findById(UUID.fromString(ticketCategoryId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
                ticketCategory.setActive(true);
                ticketCategoryRep.save(ticketCategory);
                return null;
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    @Transactional
    public Void deactivate(@RequestParam String ticketCategoryId, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(user.isAdmin() || user.hasRole("API_TICKET_CATEGORY_MANAGER")){
            try {
                var ticketCategory = ticketCategoryRep.findById(UUID.fromString(ticketCategoryId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
                ticketCategory.setActive(false);
                ticketCategoryRep.save(ticketCategory);
                return null;
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
