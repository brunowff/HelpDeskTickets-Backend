/**
 * Represents a group entity in the Help Desk Tickets application.
 * This entity is mapped to the "tb_groups" table in the database.
 * 
 * Each group has a unique identifier, a name, a description, and an active status.
 * 
 * Annotations used:
 * 
 * - {@link Entity} - Specifies that this class is an entity and is mapped to a database table.
 * - {@link Table} - Specifies the name of the database table to be used for mapping.
 * - {@link Id} - Specifies the primary key of an entity.
 * - {@link GeneratedValue} - Provides for the specification of generation strategies for the values of primary keys.
 * - {@link Column} - Used to specify the mapped column for a persistent property or field.
 * - {@link Data} - A Lombok annotation to generate getters, setters, toString, equals, and hashCode methods.
 * - {@link AllArgsConstructor} - A Lombok annotation to generate a constructor with 1 parameter for each field in the class.
 * - {@link NoArgsConstructor} - A Lombok annotation to generate a no-args constructor.
 * 
 * Fields:
 * 
 * - {@code groupId} - The unique identifier for the group, generated using UUID strategy.
 * - {@code name} - The name of the group, which must be unique.
 * - {@code description} - A brief description of the group.
 * - {@code active} - A boolean indicating whether the group is active.
 * 
 * Implements {@link Serializable} to allow the entity to be serialized.
 * 
 * @see Serializable
 * @see Entity
 * @see Table
 * @see Id
 * @see GeneratedValue
 * @see Column
 * @see Data
 * @see AllArgsConstructor
 * @see NoArgsConstructor
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.entities;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

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
@Table(name = "tb_groups")
public class Group implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "group_id")
    private UUID groupId;
    
    @Column(unique = true)
    private String name;
    private String description;
    private Boolean active;

}
