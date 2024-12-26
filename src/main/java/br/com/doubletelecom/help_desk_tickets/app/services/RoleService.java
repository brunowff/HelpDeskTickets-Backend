package br.com.doubletelecom.help_desk_tickets.app.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Role;
import br.com.doubletelecom.help_desk_tickets.app.repositories.RoleRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoleService {
	private static final Logger LOG = LoggerFactory.getLogger(RoleService.class);

	@Autowired
	private RoleRepository roleReposiroty;

	public Role save(Role role) {
		return roleReposiroty.save(role);
	}

	public List<Role> findAllRole() {
		return roleReposiroty.findAll();
	}

	public Role findDefaultRole() {
		return findAllRole().stream().findFirst().orElse(null);
	}

	public Role findRoleByName(String role) {
		return findAllRole().stream().filter(r -> r.getName().equals(role)).findFirst().orElse(null);
	}

}