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
                .route("user-service", route -> route
                        .path("/api/v1/users/**", "/api/v1/admin/users/**")
                        .uri(properties.userServiceUri()))
                .route("business-external", route -> route
                        .path("/api/v1/external/**")
                        .uri(properties.businessServiceUri()))
                .route("auth-openapi", route -> route
                        .path("/v3/api-docs/auth")
                        .filters(filter -> filter.rewritePath("/v3/api-docs/auth", "/v3/api-docs"))
                        .uri(properties.authServiceUri()))
                .route("users-openapi", route -> route
                        .path("/v3/api-docs/users")
                        .filters(filter -> filter.rewritePath("/v3/api-docs/users", "/v3/api-docs"))
                        .uri(properties.userServiceUri()))
                .route("business-openapi", route -> route
                        .path("/v3/api-docs/business")
                        .filters(filter -> filter.rewritePath("/v3/api-docs/business", "/v3/api-docs"))
                        .uri(properties.businessServiceUri()))
                .build();
    }
}
