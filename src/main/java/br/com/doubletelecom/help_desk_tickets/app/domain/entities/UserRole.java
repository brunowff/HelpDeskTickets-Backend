/**
 * Represents the association between a User and a Role in the system.
 * This entity maps to the "tb_users_roles" table in the database.
 * 
 * <p>Each UserRole instance links a specific user to a specific role,
 * allowing for the assignment of roles to users.</p>
 * 
 * <p>Annotations:</p>
 * <ul>
 *   <li>@Data - Generates getters, setters, toString, equals, and hashCode methods.</li>
 *   <li>@Entity - Specifies that the class is an entity and is mapped to a database table.</li>
 *   <li>@Table - Specifies the name of the database table to be used for mapping.</li>
 *   <li>@AllArgsConstructor - Generates a constructor with one parameter for each field in the class.</li>
 *   <li>@NoArgsConstructor - Generates a no-argument constructor.</li>
 *   <li>@JsonIgnoreProperties - Ignores unknown properties during JSON deserialization.</li>
 * </ul>
 * 
 * <p>Fields:</p>
 * <ul>
 *   <li>id - The unique identifier for the UserRole entity. It is auto-generated.</li>
 *   <li>user - The user associated with this UserRole. It is a many-to-one relationship.</li>
 *   <li>role - The role associated with this UserRole. It is a many-to-one relationship.</li>
 * </ul>
 * 
 * <p>Implements Serializable to allow instances of this class to be serialized.</p>
 */

package br.com.doubletelecom.help_desk_tickets.app.domain.entities;

import java.io.Serializable;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "tb_users_roles")
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRole implements Serializable{

	private static final long serialVersionUID = 5926468583005150707L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id", insertable = true, updatable = true)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "role_id", insertable = true, updatable = true)
	private Role role;
	

}
