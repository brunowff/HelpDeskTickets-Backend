package br.com.doubletelecom.help_desk_tickets.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    User findByUsername(String username);
    User findUserByUsernameAndPassword(String username, String password);
}
