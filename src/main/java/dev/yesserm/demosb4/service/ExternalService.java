package dev.yesserm.demosb4.service;

import dev.yesserm.demosb4.client.ExternalApiClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ExternalService {

    private final ExternalApiClient client;

    public ExternalService(ExternalApiClient client) {
        this.client = client;
    }

    public List<Map<String, Object>> getPosts() {
        return client.getPosts();
    }
}

