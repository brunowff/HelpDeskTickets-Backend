/**
 * Configuration class for creating an admin user with default privileges in the database.
 * This class implements CommandLineRunner to execute the user creation logic on application startup.
 * 
 * The admin user is created with the following details:
 * - Full name: Administrador do Sistema
 * - Username: admin
 * - Email: admin@youremail.com.br
 * - Password: Y0urP@$$worD (encoded using BCryptPasswordEncoder)
 * - Active status: true
 * - Roles: API_ADMIN
 * 
 * If the admin user already exists, a message is printed to the console.
 * 
 * Dependencies:
 * - RoleRepository: Repository for accessing role data.
 * - UserRepository: Repository for accessing user data.
 * - BCryptPasswordEncoder: Encoder for encoding the admin user's password.
 * 
 * An exception is thrown if the API_ADMIN role is not found in the database.
 * 
 * @param roleRep RoleRepository instance for accessing role data.
 * @param userRep UserRepository instance for accessing user data.
 * @param passwordEncoder BCryptPasswordEncoder instance for encoding passwords.
 */
package br.com.doubletelecom.help_desk_tickets.app.configurations;

import java.util.HashSet;
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

        // Carrega todas as roles existentes para atribuir ao admin
        //var roleAdmin = roleRep.findByName(Role.Values.API_ADMIN.name()).orElseThrow( () -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR));
        Set<Role> roles = new HashSet<>(roleRep.findAll());
        var userAdmin = userRep.findByUsername("admin");

        // Cria o usuário admin apenas se ainda não existir (idempotente)
        userAdmin.ifPresentOrElse(
                user -> {
                    System.out.println(user.getUsername() + ": Admin user alredy exists!");
                },
                () -> {
                    var user = new User();
                    user.setFullname("Administrador do Sistema");
                    user.setUsername("admin");
                    user.setEmail("admin@youremail.com.br");
                    // Senha codificada com BCrypt — alterar em produção via endpoint de reset
                    user.setPassword(passwordEncoder.encode("Y0urP@$$worD"));
                    user.setActive(true);
                    user.setRoles(roles); // Admin recebe todas as roles disponíveis
                    userRep.save(user);
                }
        );
    }

}
