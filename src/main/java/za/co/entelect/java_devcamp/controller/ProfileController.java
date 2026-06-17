package za.co.entelect.java_devcamp.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import za.co.entelect.java_devcamp.service.ProfileService;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

	private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

	private final ProfileService profileService;

	public ProfileController(ProfileService profileService) {
		this.profileService = profileService;
	}

	@GetMapping
	public Map<String, Object> getProfile(@AuthenticationPrincipal Jwt jwt) {
		String email = jwt.getSubject();
		logger.info("Profile requested for email={}", email);
		return profileService.loadProfile(email);
	}

}
