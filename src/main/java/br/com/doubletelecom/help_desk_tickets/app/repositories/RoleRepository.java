package br.com.doubletelecom.help_desk_tickets.app.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Role;



@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	Role findByName(String name);

}