package dev.yesserm.demosb4.controller;

import dev.yesserm.demosb4.dto.ApiErrorResponse;
import dev.yesserm.demosb4.dto.LoginRequest;
import dev.yesserm.demosb4.dto.LoginResponse;
import dev.yesserm.demosb4.dto.RefreshTokenRequest;
import dev.yesserm.demosb4.dto.RegisterRequest;
import dev.yesserm.demosb4.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Login, registration and token renewal endpoints.")
@SecurityRequirements
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Validates user credentials and returns a JWT access token with a refresh token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentication completed.",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "tokens": {
                                        "accessToken": "eyJhbGciOiJIUzI1NiJ9.access-token",
                                        "refreshToken": "eyJhbGciOiJIUzI1NiJ9.refresh-token",
                                        "tokenType": "Bearer",
                                        "expiresIn": 900
                                      },
                                      "user": {
                                        "id": 1,
                                        "name": "Admin User",
                                        "email": "admin@example.com",
                                        "active": true,
                                        "roles": ["ADMIN"]
                                      }
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid request body.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/register")
    @Operation(summary = "Register user", description = "Creates a standard user account and returns authentication tokens.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User registered.",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or invalid user data.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already exists.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public LoginResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/register-admin")
    @Operation(summary = "Register administrator", description = "Creates an administrator account using the setup key header.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Administrator registered.",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation error or invalid setup request.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Invalid setup key.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email already exists.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public LoginResponse registerAdmin(
            @Parameter(description = "Administrative setup key configured for first-time admin creation.", required = true,
                    example = "setup-key-value")
            @RequestHeader("X-Admin-Setup-Key") String setupKey,
            @Valid @RequestBody RegisterRequest request
    ) {
        return authService.registerAdmin(request, setupKey);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token", description = "Exchanges a valid refresh token for a new JWT token pair.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tokens renewed.",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Refresh token is missing, invalid or expired.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected server error.", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    public LoginResponse refresh(@Valid @RequestBody RefreshTokenRequest request) {
        return authService.refresh(request);
    }
}
