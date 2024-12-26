/**
 * Represents an Active Directory (AD) user entity.
 * This class is annotated with LDAP entry and attribute mappings.
 * 
 * Annotations:
 * - @Data: Generates getters, setters, toString, equals, and hashCode methods.
 * - @NoArgsConstructor: Generates a no-argument constructor.
 * - @AllArgsConstructor: Generates a constructor with arguments for all fields.
 * - @Builder: Provides a builder pattern for object creation.
 * - @Entry: Specifies the LDAP object classes for this entity.
 * 
 * Fields:
 * - id: The unique identifier for the AD user.
 * - cn: The common name of the AD user.
 * - password: The password of the AD user.
 * - sn: The surname of the AD user.
 */

package br.com.doubletelecom.help_desk_tickets.app.domain.entities;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import lombok.Builder;
import lombok.Data;
import javax.naming.*;

@Data
@Builder
@Entry(objectClasses = { "person", "top", "inetOrgPerson" })
public final class ADUser {
    @Id
	private Name id;

	@Attribute(name="cn")
	private String cn;

	@Attribute(name="password") 
	private String password;
	
	@Attribute(name="sn")
	private String sn;
}
