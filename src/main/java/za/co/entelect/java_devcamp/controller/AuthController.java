package za.co.entelect.java_devcamp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import za.co.entelect.java_devcamp.dto.LoginRequest;
import za.co.entelect.java_devcamp.dto.LoginResponse;
import za.co.entelect.java_devcamp.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "User login and JWT token endpoints")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(
			summary = "Login",
			description = "Authenticates a user against auth.application_user and returns a JWT",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					required = true,
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = LoginRequest.class),
							examples = @ExampleObject(
									name = "default",
									value = """
											{
											  "email": "products@entelect.co.za",
											  "password": "SpringProducts01$"
											}
											"""))))
	@ApiResponse(responseCode = "200", description = "Login successful", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
	@ApiResponse(responseCode = "401", description = "Invalid email or password")
	public LoginResponse login(@RequestBody LoginRequest request) {
		logger.info("Login requested for email={}", request.getEmail());
		return authService.login(request);
	}

}
