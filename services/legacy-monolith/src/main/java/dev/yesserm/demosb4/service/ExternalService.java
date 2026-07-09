package dev.yesserm.demosb4.service;

import dev.yesserm.demosb4.client.ExternalApiClient;
import dev.yesserm.demosb4.exception.ExternalApiException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Map;

@Service
public class ExternalService {

    private final ExternalApiClient client;

    public ExternalService(ExternalApiClient client) {
        this.client = client;
    }

    public List<Map<String, Object>> getPosts() {
        try {
            return client.getPosts();
        } catch (WebClientResponseException ex) {
            throw new ExternalApiException("External API returned status " + ex.getStatusCode(), ex);
        } catch (WebClientRequestException ex) {
            throw new ExternalApiException("External API request failed", ex);
        }
    }
}

