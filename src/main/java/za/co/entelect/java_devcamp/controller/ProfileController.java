package za.co.entelect.java_devcamp.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import za.co.entelect.java_devcamp.service.ProfileService;

@RestController
@RequestMapping("/api/v1/profile")
@Tag(name = "Profile", description = "Authenticated user profile endpoints")
public class ProfileController {

	private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

	private final ProfileService profileService;

	public ProfileController(ProfileService profileService) {
		this.profileService = profileService;
	}

	@GetMapping
	@Operation(
			summary = "Get authenticated user profile",
			description = "Returns the application user record and linked CIS customer profile",
			security = @SecurityRequirement(name = "bearer-jwt"))
	@ApiResponse(responseCode = "200", description = "Profile retrieved successfully", content = @Content(schema = @Schema(additionalProperties = Schema.AdditionalPropertiesValue.TRUE, example = "{\"user_id\": 1, \"email\": \"admin@entelect.co.za\", \"role\": \"user\", \"customer\": {\"customer_id\": 3, \"email\": \"admin@entelect.co.za\", \"customer_types_id\": 5}}")))
	@ApiResponse(responseCode = "401", description = "Not authenticated")
	@ApiResponse(responseCode = "404", description = "User not found")
	public Map<String, Object> getProfile(@AuthenticationPrincipal Jwt jwt) {
		String email = jwt.getSubject();
		logger.info("Profile requested for email={}", email);
		return profileService.loadProfile(email);
	}

}
