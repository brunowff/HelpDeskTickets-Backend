/**
 * Represents a role entity in the system.
 * This entity is mapped to the "tb_roles" table in the database.
 * 
 * <p>Each role has an ID and a name. The ID is automatically generated
 * using the IDENTITY strategy.</p>
 * 
 * <p>This class includes the following fields:</p>
 * <ul>
 *   <li>{@code id} - The unique identifier for the role.</li>
 *   <li>{@code name} - The name of the role.</li>
 * </ul>
 * 
 * <p>It also provides getter and setter methods for these fields.</p>
 * 
 * <p>Annotations used:</p>
 * <ul>
 *   <li>{@code @Data} - Lombok annotation to generate getters, setters, and other utility methods.</li>
 *   <li>{@code @AllArgsConstructor} - Lombok annotation to generate a constructor with all fields.</li>
 *   <li>{@code @NoArgsConstructor} - Lombok annotation to generate a no-argument constructor.</li>
 *   <li>{@code @Entity} - Specifies that the class is an entity and is mapped to a database table.</li>
 *   <li>{@code @Table(name = "tb_roles")} - Specifies the table name in the database.</li>
 *   <li>{@code @Id} - Specifies the primary key of the entity.</li>
 *   <li>{@code @GeneratedValue(strategy = GenerationType.IDENTITY)} - Specifies the generation strategy for the primary key.</li>
 *   <li>{@code @Column(name = "role_id")} - Specifies the column name for the primary key.</li>
 * </ul>
 * 
 * <p>Implements {@code Serializable} to allow the object to be serialized.</p>
 * 
 * @see java.io.Serializable
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
    private Long id;
    /**
     * The name of the role.
     */
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
