
package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateUserDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.UserDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.PageItemUserDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Role;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;
import br.com.doubletelecom.help_desk_tickets.app.repositories.RoleRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.UserRepository;
import br.com.doubletelecom.help_desk_tickets.app.services.UserServices;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserServices{

    private final UserRepository userRep;
    private final RoleRepository roleRep;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Page<PageItemUserDto> findAll(Pageable pageable){  
        return userRep.findAll(pageable).map(PageItemUserDto::new);
    }

    @Override
    @Transactional
    public User save(@RequestBody @Valid CreateUserDto userDto){
        
        var roleBasic = roleRep.findByName(Role.Values.API_BASIC.name()).orElse(null);
        var userFromDb = userRep.findByUsername(userDto.username());

        // Check if users already exist.
        if(userFromDb.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = new User();
        
        user.setFullname(userDto.fullname());
        user.setUsername(userDto.username());
        user.setEmail(userDto.email());
        // Password encoding.
        user.setPassword(passwordEncoder.encode(userDto.password()));
        user.setActive(true);
        user.setRoles(Set.of(roleBasic));

        return userRep.save(user);
    }

    @Override
    @Transactional
    public User updateUser(@RequestBody @Valid UserDto userDto, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    
        if(user.isAdmin() || user.getUserId().equals(userDto.userId())){
            user.setFullname(userDto.fullname());
            user.setUsername(userDto.username());
            user.setEmail(userDto.email());
            user.setPassword(passwordEncoder.encode(userDto.password()));
        return userRep.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    @Transactional
    public Void addRoleToUser(@RequestParam String userId, @RequestParam String roleName, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var role2add = roleRep.findByName(roleName).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var roles = user.getRoles();
       
        if(!user.isAdmin()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        roles.add(role2add);
        user.setRoles(roles);
        userRep.save(user);

        return null;
    }

    @Override
    @Transactional
    public Void removeRoleFromUser(@RequestParam String userId, @RequestParam String roleName, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var role2remove = roleRep.findByName(roleName).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var roles = user.getRoles();
        
        if(!user.isAdmin()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        try {
            roles.remove(role2remove);
            user.setRoles(roles);
            userRep.save(user);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }
        
        return null;
    }

    @Override
    @Transactional
    public Void passwordReset(@RequestBody @Valid UserDto userDto, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var user2update = userRep.findById(userDto.userId()).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(user.isAdmin() || user.getUserId().equals(userDto.userId())){
            user2update.setPassword(passwordEncoder.encode("Metro@2025"));
            userRep.save(user2update);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return null;
    }

    @Override
    @Transactional
    public Void activate(@RequestParam String userId, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var user2activate = userRep.findById(UUID.fromString(userId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(user.isAdmin()){
            user2activate.setActive(true);
            userRep.save(user2activate);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return null;
    }

    @Override
    @Transactional
    public Void deactivate(@RequestParam String userId, JwtAuthenticationToken token){
        
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var user2deactivate = userRep.findById(UUID.fromString(userId)).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if(user.isAdmin() && !user2deactivate.isAdmin()){
            user2deactivate.setActive(false);
            userRep.save(user2deactivate);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return null;
    }

}
