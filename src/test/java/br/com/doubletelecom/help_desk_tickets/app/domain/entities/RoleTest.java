package br.com.doubletelecom.help_desk_tickets.app.domain.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {

    @Test
    public void testRoleConstructorAndGetters() {
        Role role = new Role(1L, "ADMIN");
        assertEquals(1L, role.getRoleId());
        assertEquals("ADMIN", role.getName());
    }

    @Test
    public void testRoleSetters() {
        Role role = new Role();
        role.setRoleId(2L);
        role.setName("USER");
        assertEquals(2L, role.getRoleId());
        assertEquals("USER", role.getName());
    }

    @Test
    public void testRoleEnumValues() {
        assertEquals(1L, Role.Values.API_ADMIN.getRoleId());
        assertEquals(2L, Role.Values.API_BASIC.getRoleId());
        assertEquals(3L, Role.Values.API_GROUP.getRoleId());
        assertEquals(4L, Role.Values.API_GROUP_MANAGER.getRoleId());
        assertEquals(5L, Role.Values.API_USER.getRoleId());
        assertEquals(6L, Role.Values.API_USER_MANAGER.getRoleId());
        assertEquals(7L, Role.Values.API_ROLE.getRoleId());
        assertEquals(8L, Role.Values.API_ROLE_MANAGER.getRoleId());
        assertEquals(9L, Role.Values.API_TICKET.getRoleId());
        assertEquals(10L, Role.Values.API_TICKET_MANAGER.getRoleId());
        assertEquals(11L, Role.Values.API_TICKET_CATEGORY.getRoleId());
        assertEquals(12L, Role.Values.API_TICKET_CATEGORY_MANAGER.getRoleId());
        assertEquals(13L, Role.Values.API_TICKET_MESSAGE.getRoleId());
        assertEquals(14L, Role.Values.API_TICKET_MESSAGE_MANAGER.getRoleId());
        assertEquals(15L, Role.Values.API_TICKET_LOG.getRoleId());
        assertEquals(16L, Role.Values.API_TICKET_LOG_MANAGER.getRoleId());
    }
}