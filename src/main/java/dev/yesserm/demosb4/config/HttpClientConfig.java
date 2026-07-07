package dev.yesserm.demosb4.config;

import dev.yesserm.demosb4.client.ExternalApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpClientConfig {
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }

    @Bean
    public HttpServiceProxyFactory httpServiceProxyFactory(WebClient webClient) {
        WebClientAdapter adapter = WebClientAdapter.create(webClient);

        return HttpServiceProxyFactory.builder()
                .exchangeAdapter(adapter)
                .build();
    }


    @Bean
    public ExternalApiClient externalApiClient(HttpServiceProxyFactory factory) {
        return factory.createClient(ExternalApiClient.class);
    }
}
