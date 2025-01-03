package br.com.doubletelecom.help_desk_tickets.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{

    public Optional<Role> findByName(String roleName);

}
