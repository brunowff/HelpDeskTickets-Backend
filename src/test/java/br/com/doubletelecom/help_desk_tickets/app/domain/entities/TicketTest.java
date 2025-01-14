package br.com.doubletelecom.help_desk_tickets.app.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TicketTest {

    private Ticket ticket;
    private User user;
    private TicketCategory ticketCategory;
    private User attribuitedToUser;

    @BeforeEach
    public void setUp() {
        user = new User();
        ticketCategory = new TicketCategory();
        attribuitedToUser = new User();
        ticket = new Ticket(
            UUID.randomUUID(),
            "Sample Title",
            "Sample Description",
            Ticket.ValuesOfTicketStatus.ABERTO.getTicketStatus(),
            Ticket.ValuesOfPriority.HIGH.getTicketPriority(),
            user,
            ticketCategory,
            attribuitedToUser,
            Instant.now(),
            null
        );
    }

    @Test
    public void testTicketCreation() {
        assertNotNull(ticket);
        assertEquals("Sample Title", ticket.getTicketTitle());
        assertEquals("Sample Description", ticket.getTicketDescription());
        assertEquals(Ticket.ValuesOfTicketStatus.ABERTO.getTicketStatus(), ticket.getTicketStatus());
        assertEquals(Ticket.ValuesOfPriority.HIGH.getTicketPriority(), ticket.getTicketPriority());
        assertEquals(user, ticket.getUser());
        assertEquals(ticketCategory, ticket.getTicketCategory());
        assertEquals(attribuitedToUser, ticket.getAttribuitedToUser());
        assertNotNull(ticket.getCreationDateTime());
    }

    @Test
    public void testTicketStatusEnum() {
        assertEquals("ABERTO", Ticket.ValuesOfTicketStatus.ABERTO.getTicketStatus());
        assertEquals("ACEITE", Ticket.ValuesOfTicketStatus.ACEITE.getTicketStatus());
        assertEquals("FINALIZADO", Ticket.ValuesOfTicketStatus.FINALIZADO.getTicketStatus());
        assertEquals("CANCELADO", Ticket.ValuesOfTicketStatus.CANCELADO.getTicketStatus());
    }

    @Test
    public void testTicketPriorityEnum() {
        assertEquals("HIGH", Ticket.ValuesOfPriority.HIGH.getTicketPriority());
        assertEquals("MEDIUM", Ticket.ValuesOfPriority.MEDIUM.getTicketPriority());
        assertEquals("LOW", Ticket.ValuesOfPriority.LOW.getTicketPriority());
    }
}