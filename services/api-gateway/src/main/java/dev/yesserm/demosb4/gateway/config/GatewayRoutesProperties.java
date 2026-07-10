package dev.yesserm.demosb4.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.routes")
public record GatewayRoutesProperties(
        String authServiceUri,
        String userServiceUri,
        String businessServiceUri
) {
}
