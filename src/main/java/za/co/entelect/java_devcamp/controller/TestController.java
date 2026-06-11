package za.co.entelect.java_devcamp.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Health", description = "Application health and availability checks")
public class TestController {

	@GetMapping("/test")
	@Operation(summary = "Test endpoint", description = "Verifies that the application is running and accepting requests")
	@ApiResponse(responseCode = "200", description = "Application is available", content = @Content(schema = @Schema(example = "{\"message\": \"endpoint is available\"}")))
	public Map<String, String> test() {
		return Map.of("message", "endpoint is available");
	}

}
