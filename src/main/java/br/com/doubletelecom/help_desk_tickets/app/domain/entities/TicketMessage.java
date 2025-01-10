/**
 * Represents a message associated with a ticket in the help desk system.
 * This entity is mapped to the "tb_ticket_messages" table in the database.
 * Each message is associated with a specific ticket and user, and includes
 * a timestamp indicating when the message was created.
 * 
 * Annotations:
 * - @Entity: Specifies that the class is an entity and is mapped to a database table.
 * - @Table: Specifies the name of the database table to be used for mapping.
 * - @Id: Specifies the primary key of an entity.
 * - @GeneratedValue: Provides for the specification of generation strategies for the values of primary keys.
 * - @Column: Used to specify the mapped column for a persistent property or field.
 * - @ManyToOne: Defines a many-to-one relationship between two entities.
 * - @JoinColumn: Specifies a column for joining an entity association or element collection.
 * - @CreationTimestamp: Automatically populates the annotated field with the timestamp of the entity's creation.
 * - @Temporal: Specifies the temporal type (date, time, timestamp) of a persistent property or field.
 * - @Data: A Lombok annotation to generate getters, setters, toString, equals, and hashCode methods.
 * - @AllArgsConstructor: A Lombok annotation to generate a constructor with one parameter for each field.
 * - @NoArgsConstructor: A Lombok annotation to generate a no-argument constructor.
 * 
 * Fields:
 * - ticketMessageId (UUID): The unique identifier for the ticket message.
 * - message (String): The content of the message.
 * - ticket (Ticket): The ticket associated with this message.
 * - user (User): The user who created the message.
 * - messageDateTime (Date): The timestamp when the message was created.
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.entities;

import java.io.Serializable;
import java.util.Date;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_ticket_messages")
public class TicketMessage implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ticket_message_id")
    private UUID ticketMessageId;
    
    private String message;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date messageDateTime;
}
