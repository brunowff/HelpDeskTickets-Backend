/**
 * Represents a user entity in the Help Desk Tickets application.
 * This class is annotated with JPA annotations to map it to the "tb_users" table in the database.
 * It includes fields for user details such as userId, fullname, username, email, token, password, and active status.
 * It also establishes many-to-many relationships with roles and groups.
 * 
 * The class provides methods to:
 * - Check if the login credentials are correct.
 * - Determine if the user has admin privileges.
 * - Check if the user has a specific role.
 * - Check if the user belongs to a specific group.
 * 
 * Annotations:
 * - @Data: Generates getters, setters, toString, equals, and hashCode methods.
 * - @AllArgsConstructor: Generates a constructor with all fields as parameters.
 * - @NoArgsConstructor: Generates a no-argument constructor.
 * - @Entity: Specifies that the class is an entity and is mapped to a database table.
 * - @Table: Specifies the name of the database table to be used for mapping.
 * - @Id: Specifies the primary key of the entity.
 * - @GeneratedValue: Provides the specification of generation strategies for the primary key values.
 * - @Column: Specifies the mapped column for a persistent property or field.
 * - @ManyToMany: Defines a many-to-many relationship.
 * - @JoinTable: Specifies the join table for many-to-many relationships.
 * 
 * Implements:
 * - Serializable: Allows the object to be converted to a byte stream.
 */

package br.com.doubletelecom.help_desk_tickets.app.domain.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.doubletelecom.help_desk_tickets.app.domain.dtos.LoginRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_users")
public class User implements Serializable {
   @Serial 
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "user_id")
    private UUID userId;
    private String fullname;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;
    private String password; // Encoded
    
    @Column(unique = true)
    private String token; // Encoded
    private Boolean active;

    // Setting up a bundle of rules.
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "tb_users_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

     // Setting up a bundle of rules.
     @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
     @JoinTable(name = "tb_user_groups",
         joinColumns = @JoinColumn(name = "user_id"),
         inverseJoinColumns = @JoinColumn(name = "group_id"))
     private Set<Group> groups;

    // Checke login by the username and password.
    public boolean isLoginCorrect(LoginRequest loginReq, PasswordEncoder passwordEncoder){
        return passwordEncoder.matches(loginReq.password(), this.password);
    }

    public Boolean isAdmin(){
        return this.roles.stream().anyMatch(role -> role.getName().equals("API_ADMIN"));
    }

    public Boolean hasRole(String roleName){
        return this.roles.stream().anyMatch(role -> role.getName().equals(roleName));
    }
    
    public Boolean hasGroup(UUID groupId){
        return this.groups.stream().anyMatch(group -> group.getGroupId().equals(groupId));
    }
}
