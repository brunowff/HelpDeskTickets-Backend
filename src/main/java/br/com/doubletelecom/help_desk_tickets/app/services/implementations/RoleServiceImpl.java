package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Role;
import br.com.doubletelecom.help_desk_tickets.app.repositories.RoleRepository;
import br.com.doubletelecom.help_desk_tickets.app.services.RoleServices;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleServices{

    private final RoleRepository roleRep;

    @Override
    @Transactional
    public List<Role> findAll(){
        return roleRep.findAll();
    }
}
