package za.co.entelect.java_devcamp.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Test endpoint")
public class TestController {

	private static final Logger logger = LoggerFactory.getLogger(TestController.class);

	@GetMapping("/test")
	@Operation(summary = "Test endpoint", description = "Quick check to see if app is running")
	@ApiResponse(responseCode = "200", description = "Application is available", content = @Content(schema = @Schema(example = "{\"message\": \"endpoint is available\"}")))
	public Map<String, String> test() {
		logger.info("Test endpoint requested");
		return Map.of("message", "endpoint is available");
	}


}
