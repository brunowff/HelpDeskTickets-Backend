package br.com.doubletelecom.help_desk_tickets.app.configurations;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.Role;
import br.com.doubletelecom.help_desk_tickets.app.domain.entities.User;
import br.com.doubletelecom.help_desk_tickets.app.repositories.RoleRepository;
import br.com.doubletelecom.help_desk_tickets.app.repositories.UserRepository;

import jakarta.transaction.Transactional;

/*
 * Admin User creation on database with default privleges
 */

@Configuration
public class AdminUserConfig implements CommandLineRunner{

    private RoleRepository roleRep;
    private UserRepository userRep;
    private BCryptPasswordEncoder passwordEncoder;

    public AdminUserConfig(RoleRepository roleRep, UserRepository userRep, BCryptPasswordEncoder passwordEncoder) {
        this.roleRep = roleRep;
        this.userRep = userRep;
        this.passwordEncoder = passwordEncoder;
    }
    
    @Override
    @Transactional
    public void run(String... args) throws Exception{

        var roleAdmin = roleRep.findByName(Role.Values.API_ADMIN.name()).orElseThrow( () -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        var userAdmin = userRep.findByUsername("admin");

        // Check if admin is present or create it.
        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println(user.getUsername() + ": Admin user alredy exists!");
                },
                () -> {
                    var user = new User();
                    user.setFullname("Administrador do Sistema");
                    user.setUsername("admin");
                    user.setEmail("admin@doubletelecom.com.br");
                    user.setPassword(passwordEncoder.encode("M3tr0T3l3c0m"));
                    user.setRoles(Set.of(roleAdmin));
                    userRep.save(user);
                }
        );
    }

}
