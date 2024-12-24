package br.com.doubletelecom.help_desk_tickets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;

@EnableLdapRepositories(basePackages = "br.com.doubletelecom.help_desk_tickets.app.repositories.ldap.**")
@SpringBootApplication
public class HelpDeskTicketsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelpDeskTicketsApplication.class, args);
	}

}
