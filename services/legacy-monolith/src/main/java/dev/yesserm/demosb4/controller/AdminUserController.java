package dev.yesserm.demosb4.controller;

import dev.yesserm.demosb4.config.OpenAPIConfig;
import dev.yesserm.demosb4.dto.ApiErrorResponse;
import dev.yesserm.demosb4.dto.ChangeRoleRequest;
import dev.yesserm.demosb4.dto.UserDTO;
import dev.yesserm.demosb4.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/users")
@Tag(name = "Administration", description = "Administrative user management operations.")
@SecurityRequirement(name = OpenAPIConfig.BEARER_AUTH)
public class AdminUserController {
    private final UserManagementService userManagementService;

    public AdminUserController(UserManagementService userManagementService) {
        this.userManagementService = userManagementService;
    }

    @PostMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change user role", description = "Assigns a role to a user. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Role changed.",
                    content = @Content(schema = @Schema(implementation = UserDTO.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "id": 2,
                                      "name": "Ana Martinez",
                                      "email": "ana.martinez@example.com",
                                      "active": true,
                                      "roles": ["ADMIN"],
                                      "updatedBy": "admin@example.com"
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid role request.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated user is not an administrator.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User or role not found.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public UserDTO changeRole(
            @Parameter(description = "User identifier.", example = "2", required = true)
            @PathVariable Long id,
            @Valid @RequestBody ChangeRoleRequest request,
            Authentication authentication
    ) {
        return userManagementService.changeRole(id, request, authentication);
    }
}
