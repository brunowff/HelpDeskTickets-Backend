package br.com.doubletelecom.help_desk_tickets.app.domain.entity;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.naming.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entry(objectClasses = { "person", "top", "inetOrgPerson" })
public final class ADPrincipal {
    @Id
	private Name id;

	@Attribute(name="cn")
	private String cn;

	@Attribute(name="password") 
	String password;
	
	@Attribute(name="sn") 
	String sn;
}
