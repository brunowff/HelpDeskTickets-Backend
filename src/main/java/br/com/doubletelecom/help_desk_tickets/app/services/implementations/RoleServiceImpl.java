package br.com.doubletelecom.help_desk_tickets.app.services.implementations;

import org.springframework.stereotype.Service;

import br.com.doubletelecom.help_desk_tickets.app.repositories.RoleRepository;
import br.com.doubletelecom.help_desk_tickets.app.services.RoleServices;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleServices{

    private final RoleRepository roleRep;

}
