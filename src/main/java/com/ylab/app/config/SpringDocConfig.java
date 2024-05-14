package com.ylab.app.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenAPI documentation using SpringDoc.
 * This class configures the OpenAPI document for the "Training Diary Y_LAB API".
 *
 * @author Raz Livinsky
 * @since 13.05.2024
 */
@Configuration
@ComponentScan(basePackages = {"org.springdoc"})
public class SpringDocConfig {

    /**
     * Defines and provides the OpenAPI document configuration with security settings and API information.
     *
     * @return the OpenAPI document representing the API documentation.
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(
                        new Components()
                                .addSecuritySchemes("bearerAuth",
                                        new SecurityScheme()
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(
                        new Info()
                                .title("Training Diary Y_LAB API")
                                .description("Demo Spring Boot Application")
                                .version("1.0")
                );
    }
}