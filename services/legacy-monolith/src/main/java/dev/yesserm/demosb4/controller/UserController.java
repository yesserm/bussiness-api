package dev.yesserm.demosb4.controller;

import dev.yesserm.demosb4.config.OpenAPIConfig;
import dev.yesserm.demosb4.dto.ApiErrorResponse;
import dev.yesserm.demosb4.dto.ChangePasswordRequest;
import dev.yesserm.demosb4.dto.PageResponse;
import dev.yesserm.demosb4.dto.SearchUserRequest;
import dev.yesserm.demosb4.dto.UpdateProfileRequest;
import dev.yesserm.demosb4.dto.UserDTO;
import dev.yesserm.demosb4.service.UserManagementService;
import dev.yesserm.demosb4.service.UserService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "Authenticated user profile and user directory operations.")
@SecurityRequirement(name = OpenAPIConfig.BEARER_AUTH)
public class UserController {
    private final UserService userService;
    private final UserManagementService userManagementService;

    public UserController(UserService userService, UserManagementService userManagementService) {
        this.userService = userService;
        this.userManagementService = userManagementService;
    }

    @GetMapping
    @Operation(summary = "List users", description = "Returns a paginated user list filtered by optional query parameters.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users returned.",
                    content = @Content(schema = @Schema(implementation = PageResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "content": [
                                        {
                                          "id": 1,
                                          "name": "Admin User",
                                          "email": "admin@example.com",
                                          "active": true,
                                          "roles": ["ADMIN"],
                                          "createdAt": "2026-07-08T21:00:00Z"
                                        }
                                      ],
                                      "page": 0,
                                      "size": 20,
                                      "totalElements": 1,
                                      "totalPages": 1,
                                      "first": true,
                                      "last": true
                                    }
                                    """))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated user is not allowed to list users.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public PageResponse<UserDTO> findAll(
            @Parameter(description = "Name fragment filter.", example = "Ana")
            @RequestParam(required = false) String name,
            @Parameter(description = "Email fragment filter.", example = "example.com")
            @RequestParam(required = false) String email,
            @Parameter(description = "Role name filter.", example = "ADMIN")
            @RequestParam(required = false) String role,
            @Parameter(description = "Active status filter.", example = "true")
            @RequestParam(required = false) Boolean active,
            @Parameter(description = "Pagination and sorting parameters. Use page, size and sort query parameters.", example = "page=0&size=20&sort=createdAt,desc")
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable
    ) {
        return userManagementService.listUsers(name, email, role, active, pageable);
    }

    @PostMapping("/search")
    @Operation(summary = "Search users", description = "Runs an advanced paginated user search using a JSON filter payload.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Users returned.", content = @Content(schema = @Schema(implementation = PageResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid search filters.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated user is not allowed to search users.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public PageResponse<UserDTO> search(
            @RequestBody SearchUserRequest request,
            @Parameter(description = "Pagination and sorting parameters. Use page, size and sort query parameters.")
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable
    ) {
        return userManagementService.searchUsers(request, pageable);
    }

    @GetMapping("/me")
    @Operation(summary = "Get current profile", description = "Returns the profile and audit metadata for the authenticated principal.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Current profile returned.", content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Authenticated user no longer exists.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public UserDTO profile(Authentication authentication) {
        return userService.getProfile(authentication);
    }

    @PutMapping("/me")
    @Operation(summary = "Update current profile", description = "Updates profile fields for the authenticated principal.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile updated.", content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validation error.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Authenticated user no longer exists.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already exists.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public UserDTO updateProfile(Authentication authentication, @Valid @RequestBody UpdateProfileRequest request) {
        return userManagementService.updateProfile(authentication, request);
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change current password", description = "Changes the password for the authenticated principal.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password changed."),
            @ApiResponse(responseCode = "400", description = "Validation error or invalid current password.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Authenticated user no longer exists.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public void changePassword(Authentication authentication, @Valid @RequestBody ChangePasswordRequest request) {
        userManagementService.changePassword(authentication, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deactivate user", description = "Deactivates a user account. Requires ADMIN role.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deactivated."),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated user is not an administrator.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public void deactivate(@PathVariable Long id) {
        userManagementService.deactivateUser(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Returns a user profile by its internal identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User returned.", content = @Content(schema = @Schema(implementation = UserDTO.class))),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Authenticated user is not allowed to read this user.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public UserDTO findById(@PathVariable Long id) {
        return userService.findById(id);
    }
}
