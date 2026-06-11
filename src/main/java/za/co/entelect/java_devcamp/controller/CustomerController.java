package za.co.entelect.java_devcamp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Customers", description = "Customer data endpoints")
public class CustomerController {

	private final JdbcTemplate jdbcTemplate;

	public CustomerController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@GetMapping("/fetchCustomers")
	@Operation(summary = "Fetch all customers", description = "Returns every customer from the CIS schema")
	@ApiResponse(responseCode = "200", description = "Customers retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(additionalProperties = Schema.AdditionalPropertiesValue.TRUE, example = "{\"id\": 1, \"name\": \"Jane Doe\"}"))))
	public List<Map<String, Object>> fetchCustomers() {
		return jdbcTemplate.queryForList("SELECT * FROM cis.customer");
	}

}
