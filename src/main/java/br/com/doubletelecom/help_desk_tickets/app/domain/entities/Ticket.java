package br.com.doubletelecom.help_desk_tickets.app.domain.entities;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
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
    private TickeCategory ticketCategory;

    @ManyToOne
    @JoinColumn(name = "attribuited_to_user_id")
    private User attribuitedToUser;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Instant creationDateTime;

    @Temporal(TemporalType.TIMESTAMP)
    Date finalizationDateTime;

    public enum ValuesOfTicketStatus {

        ABERTO("ABERTO"),
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
