package br.com.doubletelecom.help_desk_tickets.app.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SprigDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
    return new OpenAPI()
            .info(new Info()
                .title("Help Desk Tickets API")
                .version("1.0")
                .description("API documentation for Help Desk Tickets application"))
            .components(new Components()
            .addSecuritySchemes("bearer-key",
            new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }

}
