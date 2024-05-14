package com.ylab.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The AppConfig class configures the main settings for the application.
 * It facilitates the integration of AspectJ for aspect-oriented programming
 * with auto proxy capabilities and enables the scanning of components marked
 * with Spring annotations.
 * <p>
 * This configuration class also sets up the static resources for Swagger UI
 * which helps in creating a visual documentation for the RESTful APIs served by
 * the application. This allows Swagger UI to be served up through Spring MVC.
 * <p>
 * Annotations used:
 * <ul>
 *   <li>{@code @EnableWebMvc} - It enables default Spring MVC configuration and
 *       customizations through the {@code WebMvcConfigurer} interface.</li>
 *   <li>{@code @Configuration} - Indicates that the class has @Bean definition methods,
 *       so Spring container can process the class and generate Spring Beans accordingly.</li>
 *   <li>{@code @EnableAspectJAutoProxy} - Allows support for handling components through
 *       AspectJ's aspect-oriented programming capabilities including proxy object management.</li>
 * </ul>
 *
 * @author Raz Livinsky
 * @since 05.05.2024
 */
@EnableWebMvc
@Configuration
@EnableAspectJAutoProxy
public class AppConfig implements WebMvcConfigurer {

    /**
     * Configures the resource handlers to serve static resources. This method enhances the Spring MVC
     * configuration to provide web access to these resources.
     * <p>
     * Resource handlers are set up for:
     * <ul>
     *   <li>Swagger UI - Enables the serving of `swagger-ui.html` from the classpath. This is essential
     *       for Swagger API documentation to be accessible via a web browser.</li>
     *   <li>WebJars - Allows serving of library files (like JavaScript and CSS) stored in WebJars,
     *       which are packaged within JAR files.</li>
     * </ul>
     * These configurations are crucial for integrating external UI components and libraries seamlessly
     * into the Spring MVC application.
     *
     * @param registry The {@link ResourceHandlerRegistry} used to register resource handlers.
     *                 It is provided by Spring and tailored to manage the locations and paths from
     *                 which resources are served.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}