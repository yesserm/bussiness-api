package dev.yesserm.demosb4.controller;

import dev.yesserm.demosb4.config.OpenAPIConfig;
import dev.yesserm.demosb4.dto.ApiErrorResponse;
import dev.yesserm.demosb4.service.ExternalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/external")
@Tag(name = "External Integrations", description = "Endpoints backed by configured HTTP Interfaces.")
@SecurityRequirement(name = OpenAPIConfig.BEARER_AUTH)
public class ExternalController {

    private final ExternalService service;

    public ExternalController(ExternalService service) {
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
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "502", description = "External API request failed.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public List<Map<String, Object>> getPosts() {
        return service.getPosts();
    }
}

