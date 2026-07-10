package dev.yesserm.demosb4.businessservice.controller;

import dev.yesserm.demosb4.businessservice.exception.ExternalApiException;
import dev.yesserm.demosb4.businessservice.service.ExternalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/external")
@Tag(name = "External Integrations", description = "Endpoints backed by configured HTTP Interfaces.")
public class ExternalController {

    private final ExternalService service;

    ExternalController(ExternalService service) {
        this.service = service;
    }

    @GetMapping("/posts")
    @Operation(summary = "Get external posts", description = "Returns posts from the configured external API integration.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "External posts returned.",
                    content = @Content(schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(value = """
                                    [
                                      {
                                        "userId": 1,
                                        "id": 1,
                                        "title": "Example post",
                                        "body": "Example body"
                                      }
                                    ]
                                    """))),
            @ApiResponse(responseCode = "401", description = "Missing JWT at the API gateway."),
            @ApiResponse(responseCode = "502", description = "External API request failed.", content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public List<Map<String, Object>> getPosts() {
        return service.getPosts();
    }
}
