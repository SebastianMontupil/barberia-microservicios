package com.barberia.servicios.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI serviciosOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API de Servicios - Barbería")
                .description("Catálogo de servicios ofrecidos por la barbería.")
                .version("1.0.0"));
    }
}
