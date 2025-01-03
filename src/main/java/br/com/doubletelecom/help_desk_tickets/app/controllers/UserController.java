package br.com.doubletelecom.help_desk_tickets.app.controllers;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateUserDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;
import br.com.doubletelecom.help_desk_tickets.app.services.UserServices;

import lombok.AllArgsConstructor;

/*
 * End points to handle Users
 */
@RestController("/um")
@AllArgsConstructor
public class UserController {

    private final UserServices userServices;

    @PostMapping("/users")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserDto userDto){

        try {
            userServices.save(userDto);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

    }

    // Generate the User list if the requester is an admin.
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_API_ADMIN')")
    public ResponseEntity<List<User>> listUsers(){
        var users = userServices.findAll();
        return ResponseEntity.ok(users);
    }
    
}
