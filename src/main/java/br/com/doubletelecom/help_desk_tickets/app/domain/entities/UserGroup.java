/**
 * Represents a UserGroup entity that maps to the "tb_user_groups" table in the database.
 * This entity is used to associate a user with a group.
 * 
 * Annotations:
 * 
 *   {@code @Data} - Generates getters, setters, toString, equals, and hashCode methods.
 *   {@code @Entity} - Specifies that the class is an entity and is mapped to a database table.
 *   {@code @AllArgsConstructor} - Generates a constructor with one parameter for each field in the class.
 *   {@code @NoArgsConstructor} - Generates a no-argument constructor.
 *   {@code @Table(name = "tb_user_groups")} - Specifies the table name in the database.
 * 
 * 
 * Fields:
 * 
 *   {@code userGroupId} - The unique identifier for the UserGroup entity, generated using UUID strategy.
 *   {@code user} - The user associated with the group, mapped by a many-to-one relationship.
 *   {@code group} - The group associated with the user, mapped by a many-to-one relationship.
 * 
 * 
 * Implements:
 * 
 *   {@code Serializable} - Allows the entity to be serialized.
 * 
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_user_groups")
public class UserGroup implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_group_id")
    private UUID userGroupId;

    @ManyToOne
	@JoinColumn(name = "user_id", insertable = true, updatable = true)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "group_id", insertable = true, updatable = true)
	private Group group;

}
