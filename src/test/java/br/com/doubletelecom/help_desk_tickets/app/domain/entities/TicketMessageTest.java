package br.com.doubletelecom.help_desk_tickets.app.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TicketMessageTest {

    private TicketMessage ticketMessage;
    private Ticket ticket;
    private User user;

    @BeforeEach
    public void setUp() {
        ticket = new Ticket();
        user = new User();
        ticketMessage = new TicketMessage();
        ticketMessage.setTicketMessageId(UUID.randomUUID());
        ticketMessage.setMessage("Test message");
        ticketMessage.setTicket(ticket);
        ticketMessage.setUser(user);
        ticketMessage.setMessageDateTime(new Date());
    }

    @Test
    public void testTicketMessageId() {
        assertNotNull(ticketMessage.getTicketMessageId());
    }

    @Test
    public void testMessage() {
        assertEquals("Test message", ticketMessage.getMessage());
    }

    @Test
    public void testTicket() {
        assertEquals(ticket, ticketMessage.getTicket());
    }

    @Test
    public void testUser() {
        assertEquals(user, ticketMessage.getUser());
    }

    @Test
    public void testMessageDateTime() {
        assertNotNull(ticketMessage.getMessageDateTime());
    }
}