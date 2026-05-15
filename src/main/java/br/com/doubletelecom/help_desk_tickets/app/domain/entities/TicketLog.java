/**
 * Represents a log entry for a ticket in the help desk system.
 * This entity is used to track changes and actions performed on a ticket.
 * Each log entry is associated with a user and a ticket.
 * 
 * Annotations used:
 * 
 *   {@link Entity} - Specifies that this class is an entity and is mapped to a database table.
 *   {@link Table} - Specifies the name of the database table to be used for mapping.
 *   {@link Id} - Specifies the primary key of an entity.
 *   {@link GeneratedValue} - Provides for the specification of generation strategies for the values of primary keys.
 *   {@link Column} - Used to specify the mapped column for a persistent property or field.
 *   {@link ManyToOne} - Defines a many-to-one relationship between two entities.
 *   {@link JoinColumn} - Specifies a column for joining an entity association or element collection.
 *   {@link CreationTimestamp} - Automatically sets the date and time when the entity is created.
 *   {@link Data} - A Lombok annotation to generate getters, setters, toString, equals, and hashCode methods.
 *   {@link AllArgsConstructor} - A Lombok annotation to generate a constructor with one parameter for each field in the class.
 *   {@link NoArgsConstructor} - A Lombok annotation to generate a no-argument constructor.
 * 
 * 
 * Fields:
 * 
 *   {@code ticketLogId} - Unique identifier for the ticket log entry.
 *   {@code logDescription} - Description of the log entry.
 *   {@code user} - The user associated with the log entry.
 *   {@code ticket} - The ticket associated with the log entry.
 *   {@code logDateTime} - The date and time when the log entry was created.
 * 
 * 
 * Implements {@link Serializable} to allow instances of this class to be serialized.
 * 
 * @see User
 * @see Ticket
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_ticket_logs")
public class TicketLog implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ticket_log_id")
    private UUID ticketLogId;

    private String logDescription;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @CreationTimestamp
    private Instant logDateTime;


}
