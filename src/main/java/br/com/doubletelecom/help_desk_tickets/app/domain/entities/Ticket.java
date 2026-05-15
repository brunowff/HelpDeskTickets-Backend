/**
 * Represents a ticket in the help desk system.
 * This entity is mapped to the "tb_tickets" table in the database.
 * Implements Serializable interface for object serialization.
 * 
 * Fields:
 * - ticketId: Unique identifier for the ticket.
 * - ticketTitle: Title of the ticket.
 * - ticketDescription: Description of the ticket.
 * - ticketStatus: Current status of the ticket.
 * - ticketPriority: Priority level of the ticket.
 * - user: The user who created the ticket.
 * - ticketCategory: The category of the ticket.
 * - attribuitedToUser: The user to whom the ticket is assigned.
 * - creationDateTime: Timestamp when the ticket was created.
 * - finalizationDateTime: Timestamp when the ticket was finalized.
 * 
 * Enums:
 * - ValuesOfTicketStatus: Enum representing possible statuses of a ticket.
 * - ValuesOfPriority: Enum representing possible priority levels of a ticket.
 * 
 * Annotations:
 * - @Entity: Specifies that the class is an entity and is mapped to a database table.
 * - @Table: Specifies the name of the database table to be used for mapping.
 * - @Id: Specifies the primary key of an entity.
 * - @GeneratedValue: Provides for the specification of generation strategies for the values of primary keys.
 * - @Column: Used to specify the mapped column for a persistent property or field.
 * - @ManyToOne: Defines a many-to-one relationship between two entities.
 * - @JoinColumn: Specifies a column for joining an entity association or element collection.
 * - @CreationTimestamp: Automatically sets the creation timestamp.
 * 
 * Lombok Annotations:
 * - @Data: Generates getters, setters, toString, equals, and hashCode methods.
 * - @AllArgsConstructor: Generates a constructor with 1 parameter for each field in the class.
 * - @NoArgsConstructor: Generates a no-argument constructor.
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
@Table(name = "tb_tickets")
public class Ticket implements Serializable{
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ticket_id")
    private UUID ticketId;
    private String ticketTitle;
    private String ticketDescription;
    private String ticketStatus;
    private String ticketPriority;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ticket_category_id")
    private TicketCategory ticketCategory;

    @ManyToOne
    @JoinColumn(name = "attribuited_to_user_id")
    private User attribuitedToUser;

    @CreationTimestamp
    private Instant creationDateTime;

    private Instant finalizationDateTime;

    public enum ValuesOfTicketStatus {

        ABERTO("ABERTO"),
        PENDENTE("PENDENTE"),
        ACEITE("ACEITE"),
        FINALIZADO("FINALIZADO"),
        CANCELADO("CANCELADO"),
        ;

        private final String ticketStatus;

        ValuesOfTicketStatus(String ticketStatus) {
            this.ticketStatus = ticketStatus;
        }

        public String getTicketStatus() {
            return ticketStatus;
        }
    }

    public enum ValuesOfPriority {

        HIGH("HIGH"),
        MEDIUM("MEDIUM"),
        LOW("LOW"),
        ;

        private final String ticketPriority;

        ValuesOfPriority(String ticketPriority) {
            this.ticketPriority = ticketPriority;
        }

        public String getTicketPriority() {
            return ticketPriority;
        }
    }

}
