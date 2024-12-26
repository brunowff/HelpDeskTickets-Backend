package br.com.doubletelecom.help_desk_tickets.app.repositories.ldap;

import org.springframework.data.ldap.repository.LdapRepository;

import br.com.doubletelecom.help_desk_tickets.app.domain.entities.*;

public interface LDAPRepository extends LdapRepository<ADUser> {
    ADUser findByCn(String cn);
    ADUser findByCnAndPassword(String cn, String password);
}
