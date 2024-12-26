package br.com.doubletelecom.help_desk_tickets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableLdapRepositories(basePackages = "br.com.doubletelecom.help_desk_tickets.app.repositories.ldap.**")
@EnableWebMvc
public class HelpDeskTicketsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelpDeskTicketsApplication.class, args);
	}

}
