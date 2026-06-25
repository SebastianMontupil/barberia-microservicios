package com.barberia.auth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI authOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API de Autenticación - Barbería")
                .description("Gestión de usuarios, inicio de sesión y recuperación de contraseña.")
                .version("1.0.0"));
    }
}
