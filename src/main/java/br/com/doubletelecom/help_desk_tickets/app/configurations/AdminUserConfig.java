package br.com.doubletelecom.help_desk_tickets.app.configurations;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

        var roleAdmin = roleRep.findByName(Role.Values.ADMIN.name()).orElse(null);
        var userAdmin = userRep.findByUsername("admin");

        // Check if admin is present or create it.
        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println("Admin user alredy exists!");
                },
                () -> {
                    var user = new User();
                    user.setFullname("Administrador do Sistema");
                    user.setUsername("admin");
                    user.setPassword(passwordEncoder.encode("123"));
                    user.setRoles(Set.of(roleAdmin));
                    userRep.save(user);
                }
        );
    }

}
