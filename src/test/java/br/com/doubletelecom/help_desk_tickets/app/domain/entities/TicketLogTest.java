package br.com.doubletelecom.help_desk_tickets.app.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TicketLogTest {

    private TicketLog ticketLog;
    private User user;
    private Ticket ticket;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setUserId(UUID.randomUUID());
        user.setUsername("testUser");

        ticket = new Ticket();
        ticket.setTicketId(UUID.randomUUID());
        ticket.setTicketTitle("Test Ticket");

        ticketLog = new TicketLog();
        ticketLog.setTicketLogId(UUID.randomUUID());
        ticketLog.setLogDescription("Test log description");
        ticketLog.setUser(user);
        ticketLog.setTicket(ticket);
        ticketLog.setLogDateTime(new Date());
    }

    @Test
    public void testTicketLogId() {
        assertNotNull(ticketLog.getTicketLogId());
    }

    @Test
    public void testLogDescription() {
        assertEquals("Test log description", ticketLog.getLogDescription());
    }

    @Test
    public void testUser() {
        assertEquals(user, ticketLog.getUser());
    }

    @Test
    public void testTicket() {
        assertEquals(ticket, ticketLog.getTicket());
    }

    @Test
    public void testLogDateTime() {
        assertNotNull(ticketLog.getLogDateTime());
    }
}