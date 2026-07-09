package dev.yesserm.demosb4.authservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenApiConfig {

    @Bean
    OpenAPI authServiceOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("demosb4 Auth Service API")
                        .version("v1")
                        .description("Authentication, registration and refresh token endpoints."));
    }
}
