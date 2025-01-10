/**
 * Represents a role entity in the system.
 * This entity is mapped to the "tb_roles" table in the database.
 * Implements Serializable interface for object serialization.
 * 
 * Annotations:
 * - @Data: Generates getters, setters, toString, equals, and hashCode methods.
 * - @AllArgsConstructor: Generates a constructor with 1 parameter for each field in the class.
 * - @NoArgsConstructor: Generates a no-argument constructor.
 * - @Entity: Specifies that the class is an entity and is mapped to a database table.
 * - @Table: Specifies the table name in the database.
 * 
 * Fields:
 * - serialVersionUID: A unique identifier for Serializable class.
 * - roleId: The unique identifier for the role, mapped to "role_id" column.
 * - name: The name of the role, must be unique.
 * 
 * Enum Values:
 * - Represents default roles with predefined role IDs.
 * - Each enum constant has a roleId associated with it.
 * - To add new roles, update the classpath:data.sql file to insert them into the database.
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.domain.entities;

import java.io.Serial;
import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tb_roles")
public class Role implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleId;

    @Column(unique = true)
    private String name;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Default Roles Enum.
    public enum Values {

        // For add new roles is necessary to upgrade the classpath:data.sql file to insert in database to.
        API_ADMIN(1L),
        API_BASIC(2L),
        API_GROUP(3L),
        API_GROUP_MANAGER(4L),
        API_USER(5L),
        API_USER_MANAGER(6L),
        API_ROLE(7L),
        API_ROLE_MANAGER(8L),
        API_TICKET(9L),
        API_TICKET_MANAGER(10L),
        API_TICKET_CATEGORY(11L),
        API_TICKET_CATEGORY_MANAGER(12L),
        API_TICKET_MESSAGE(13L),
        API_TICKET_MESSAGE_MANAGER(14L),
        API_TICKET_LOG(15L),
        API_TICKET_LOG_MANAGER(16L),
        ;

        long roleId;

        Values(long roleId) {
            this.roleId = roleId;
        }

        public long getRoleId() {
            return roleId;
        }
    }
}
