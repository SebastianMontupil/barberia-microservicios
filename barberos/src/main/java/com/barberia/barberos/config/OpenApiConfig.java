package com.barberia.barberos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI barberosOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("API de Barberos - Barbería")
                .description("Gestión de perfiles, especialidades y disponibilidad de barberos.")
                .version("1.0.0"));
    }
}
