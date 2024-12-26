package br.com.doubletelecom.help_desk_tickets.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.UserRole;


@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long>{
	
	List<UserRole> findAllByUserId(Long id);

}