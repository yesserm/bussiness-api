package dev.yesserm.demosb4.businessservice.client;

import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.Map;

@HttpExchange
public interface ExternalApiClient {
    @GetExchange("/posts")
    List<Map<String, Object>> getPosts();
}
