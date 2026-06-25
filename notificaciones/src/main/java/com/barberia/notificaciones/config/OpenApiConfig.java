package com.barberia.notificaciones.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI notificacionesOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API de Notificaciones - Barbería")
                .description("Gestión de notificaciones asociadas a usuarios y reservas.")
                .version("1.0.0"));
    }
}
