/**
 * Represents a category of a ticket in the help desk system.
 * This entity is mapped to the "tb_ticket_types" table in the database.
 * Each ticket category has a unique identifier, a name, an active status,
 * and a reference to a destination group.
 * 
 * Annotations:
 * - @Entity: Specifies that the class is an entity and is mapped to a database table.
 * - @Table: Specifies the name of the database table to be used for mapping.
 * - @Data: Lombok annotation to generate getters, setters, toString, equals, and hashCode methods.
 * - @AllArgsConstructor: Lombok annotation to generate a constructor with all fields.
 * - @NoArgsConstructor: Lombok annotation to generate a no-argument constructor.
 * 
 * Fields:
 * - ticketCategoryId: Unique identifier for the ticket category, generated using UUID strategy.
 * - name: Name of the ticket category, must be unique.
 * - active: Boolean flag indicating whether the ticket category is active.
 * - destinationGroup: Reference to the Group entity that represents the destination group for the ticket category.
 * 
 * Implements:
 * - Serializable: Allows the object to be serialized for storage or transmission.
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_ticket_types")
public class TicketCategory implements Serializable{
    
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ticket_category_id")
    private UUID ticketCategoryId;
    @Column(unique = true)
    private String name;
    private Boolean active;
    
    @ManyToOne
    @JoinColumn(name = "group_id", insertable = true, updatable = true)
    private Group destinationGroup;

}
