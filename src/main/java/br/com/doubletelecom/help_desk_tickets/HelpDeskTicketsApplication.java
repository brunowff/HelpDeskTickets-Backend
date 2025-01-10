/**
 * The main entry point for the HelpDeskTickets Spring Boot application.
 * This class contains the main method which is used to launch the application.
 * 
 * <p>
 * The application is configured as a Spring Boot application using the
 * {@link SpringBootApplication} annotation.
 * </p>
 * 
 * <p>
 * Usage:
 * </p>
 * <pre>
 * {@code
 * java -jar help-desk-tickets.jar
 * }
 * </pre>
 * 
 * @see SpringApplication
 * @see SpringBootApplication
 * 
 * @author
 * @version
 */
package br.com.doubletelecom.help_desk_tickets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HelpDeskTicketsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelpDeskTicketsApplication.class, args);
	}

}
