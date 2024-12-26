/**
 * Represents a user entity in the Help Desk Tickets application.
 * This class is mapped to the "tb_users" table in the database.
 * It includes fields for the user's ID, username, password, and entity number.
 * 
 * Annotations:
 * - @Entity: Specifies that this class is an entity and is mapped to a database table.
 * - @Table: Specifies the name of the database table to be used for mapping.
 * - @JsonIgnoreProperties: Ignores unknown properties during JSON serialization/deserialization.
 * - @Data: Generates getters, setters, toString, equals, and hashCode methods.
 * - @AllArgsConstructor: Generates a constructor with one parameter for each field in the class.
 * - @NoArgsConstructor: Generates a no-argument constructor.
 * - @Id: Specifies the primary key of an entity.
 * - @GeneratedValue: Provides for the specification of generation strategies for the values of primary keys.
 * - @Column: Specifies the mapped column for a persistent property or field.
 * 
 * Fields:
 * - id: The unique identifier for the user. It is auto-generated.
 * - username: The username of the user. It must be unique.
 * - password: The password of the user.
 * - entityNo: The unique entity number of the user.
 */

package br.com.doubletelecom.help_desk_tickets.app.domain.entities;

import java.io.Serial;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_users")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long Id;

	@Column(name = "username", unique = true, nullable = false)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "entity_no", unique = true, nullable = false)
	private String entityNo;

}
