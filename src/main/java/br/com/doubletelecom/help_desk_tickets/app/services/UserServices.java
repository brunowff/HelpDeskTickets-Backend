/**
 * Service interface for managing users.
 * Provides methods for saving a new user and retrieving all users.
 */
package br.com.doubletelecom.help_desk_tickets.app.services;

import java.util.List;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.CreateUserDto;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;

public interface UserServices {

    User save(CreateUserDto userDto);
    List<User> findAll();

}
