package com.ajitech.teloconvierto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI().info(
            new Info()
            .title("Api de convertidor de archivos")
            .version("1.1")
            .description("Con esta API se puede administrar las conversiones de archvios, incluyendo la creación, actualización y eliminación de conversiones, así como la gestión de formatos.")
            .contact(new Contact()
                .name("AjiTech")
                .email("ajitech666@gmail.com")
                .url("https://github.com/tuusuario")
            )
        );
    }

}
