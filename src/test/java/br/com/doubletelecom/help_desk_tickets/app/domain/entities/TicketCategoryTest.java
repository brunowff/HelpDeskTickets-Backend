package br.com.doubletelecom.help_desk_tickets.app.domain.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TicketCategoryTest {

    private TicketCategory ticketCategory;
    private Group group;

    @BeforeEach
    public void setUp() {
        group = new Group();
        group.setGroupId(UUID.randomUUID());
        group.setName("Support");

        ticketCategory = new TicketCategory();
        ticketCategory.setTicketCategoryId(UUID.randomUUID());
        ticketCategory.setName("Technical Issue");
        ticketCategory.setActive(true);
        ticketCategory.setDestinationGroup(group);
    }

    @Test
    public void testTicketCategoryId() {
        assertNotNull(ticketCategory.getTicketCategoryId());
    }

    @Test
    public void testName() {
        assertEquals("Technical Issue", ticketCategory.getName());
    }

    @Test
    public void testActive() {
        assertEquals(true, ticketCategory.getActive());
    }

    @Test
    public void testDestinationGroup() {
        assertNotNull(ticketCategory.getDestinationGroup());
        assertEquals("Support", ticketCategory.getDestinationGroup().getName());
    }

    @Test
    public void testSetTicketCategoryId() {
        UUID newId = UUID.randomUUID();
        ticketCategory.setTicketCategoryId(newId);
        assertEquals(newId, ticketCategory.getTicketCategoryId());
    }

    @Test
    public void testSetName() {
        String newName = "Billing Issue";
        ticketCategory.setName(newName);
        assertEquals(newName, ticketCategory.getName());
    }

    @Test
    public void testSetActive() {
        ticketCategory.setActive(false);
        assertEquals(false, ticketCategory.getActive());
    }

    @Test
    public void testSetDestinationGroup() {
        Group newGroup = new Group();
        newGroup.setGroupId(UUID.randomUUID());
        newGroup.setName("Billing");
        ticketCategory.setDestinationGroup(newGroup);
        assertEquals(newGroup, ticketCategory.getDestinationGroup());
    }
}