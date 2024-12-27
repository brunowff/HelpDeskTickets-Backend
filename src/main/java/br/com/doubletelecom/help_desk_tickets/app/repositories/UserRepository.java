package br.com.doubletelecom.help_desk_tickets.app.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>{

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

}
