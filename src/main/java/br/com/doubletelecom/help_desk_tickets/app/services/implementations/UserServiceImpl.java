package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateUserDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Role;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;
import br.com.doubletelecom.help_desk_tickets.app.repositories.RoleRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.UserRepository;
import br.com.doubletelecom.help_desk_tickets.app.services.UserServices;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserServices{

    private final UserRepository userRep;
    private final RoleRepository roleRep;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public List<User> findAll(){
        return userRep.findAll();
    }

    @Override
    @Transactional
    public User save(@RequestBody CreateUserDto userDto){
        
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
        user.setRoles(Set.of(roleBasic));

        return userRep.save(user);
    }

    @Override
    @Transactional
    public Void addRoleToUser(String userId, String roleName, JwtAuthenticationToken token){
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var role2add = roleRep.findByName(roleName).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var roles = user.getRoles();
        
        var isAdmin = user.getRoles()
            .stream()
            .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.API_ADMIN.name()));

        if(!isAdmin){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        roles.add(role2add);
        user.setRoles(roles);
        userRep.save(user);

        return null;
    }

    @Override
    @Transactional
    public Void removeRoleFromUser(String userId, String roleName, JwtAuthenticationToken token){
        var user = userRep.findById(UUID.fromString(token.getName())).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var role2remove = roleRep.findByName(roleName).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var roles = user.getRoles();
        
        var isAdmin = user.getRoles()
            .stream()
            .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.API_ADMIN.name()));

        if(!isAdmin){
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

}
