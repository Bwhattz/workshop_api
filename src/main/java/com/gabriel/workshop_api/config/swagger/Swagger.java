package com.gabriel.workshop_api.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Swagger {


    @Bean
    public OpenAPI openAPI() {

        final String securityScheme = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("Sistema workshop")
                        .version("v1.0.0")
                        .description("Sistema de gerenciamento acâdemico")
                        .contact(new Contact()
                                .name("Gabriel Lucas")))

                .addSecurityItem(new SecurityRequirement().addList(securityScheme))
                .components(new Components()
                        .addSecuritySchemes(securityScheme, new SecurityScheme()
                                .name(securityScheme)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }
}
