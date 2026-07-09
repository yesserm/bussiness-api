package dev.yesserm.demosb4.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    public static final String BEARER_AUTH = "BearerAuth";

    @Bean
    public OpenAPI demosb4OpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Demosb4 Enterprise API")
                        .description("""
                                Enterprise API base project with JWT security, user management,
                                automatic auditing, Flyway migrations and external HTTP interfaces.
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Demosb4 API Support")
                                .email("support@example.com"))
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")))
                .components(new Components()
                        .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                                .name("Authorization")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("JWT access token using the Authorization: Bearer <token> header.")))
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
    }
}
