package za.co.entelect.java_devcamp.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import za.co.entelect.java_devcamp.dto.request.LoginRequest;
import za.co.entelect.java_devcamp.dto.response.LoginResponse;
import za.co.entelect.java_devcamp.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
	public LoginResponse login(@RequestBody LoginRequest request) {
		logger.info("Login requested for email={}", request.getEmail());
		return authService.login(request);
	}

}
