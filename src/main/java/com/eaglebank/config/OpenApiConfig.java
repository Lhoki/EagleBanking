package com.eaglebank.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Local development");

        Server prodServer = new Server();
        prodServer.setUrl("https://some.production.url.example.com");
        prodServer.setDescription("Production");

        return new OpenAPI()
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Eagle Bank API")
                        .version("v1.0.0")
                        .description("An API for setting up new users, accounts and various banking tasks."))
                .servers(List.of(devServer, prodServer))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
