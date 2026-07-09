package dev.yesserm.demosb4.ordersservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class OpenApiConfig {

    @Bean
    OpenAPI ordersServiceOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("demosb4 Orders Service API")
                        .version("v1")
                        .description("Orders service skeleton."));
    }
}
