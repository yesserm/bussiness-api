package dev.yesserm.demosb4.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class GatewayRoutesConfig {

    @Bean
    RouteLocator gatewayRoutes(RouteLocatorBuilder routes, GatewayRoutesProperties properties) {
        return routes.routes()
                .route("legacy-auth", route -> route
                        .path("/api/v1/auth/**")
                        .uri(properties.authServiceUri()))
                .route("legacy-openapi", route -> route
                        .path("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                        .uri(properties.legacyMonolithUri()))
                .route("user-service", route -> route
                        .path("/api/v1/users/**", "/api/v1/admin/users/**")
                        .uri(properties.userServiceUri()))
                .route("legacy-external", route -> route
                        .path("/api/v1/external/**")
                        .uri(properties.legacyMonolithUri()))
                .build();
    }
}
