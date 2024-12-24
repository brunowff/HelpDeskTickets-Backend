package br.com.doubletelecom.help_desk_tickets.app.repositories.ldap;

import org.springframework.data.ldap.repository.LdapRepository;
import br.com.doubletelecom.help_desk_tickets.app.domain.entity.*;

public interface LDAPRepository extends LdapRepository<ADPrincipal> {
    ADPrincipal findByCn(String cn);
    ADPrincipal findByCnAndPassword(String cn, String password);
}
