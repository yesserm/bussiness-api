package dev.yesserm.demosb4.controller;

import dev.yesserm.demosb4.service.ExternalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/external")
public class ExternalController {

    private final ExternalService service;

    public ExternalController(ExternalService service) {
        this.service = service;
    }

    @GetMapping("/posts")
    public List<Map<String, Object>> getPosts() {
        return service.getPosts();
    }
}

