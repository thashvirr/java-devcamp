package za.co.entelect.java_devcamp.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import za.co.entelect.java_devcamp.exception.CustomerNotFoundException;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Customers", description = "Customer data endpoints")
public class CustomerController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	private final JdbcTemplate jdbcTemplate;

	public CustomerController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@GetMapping("/fetchCustomers")
	@Operation(summary = "Fetch all customers", description = "Returns every customer from the CIS schema")
	@ApiResponse(responseCode = "200", description = "Customers retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(additionalProperties = Schema.AdditionalPropertiesValue.TRUE, example = "{\"customer_id\": 1, \"name\": \"Jane Doe\"}"))))
	public List<Map<String, Object>> fetchCustomers() {
		logger.info("Fetch all customers requested");
		return jdbcTemplate.queryForList("SELECT * FROM cis.customer");
	}

	@GetMapping("/fetchCustomer/{id}")
	@Operation(summary = "Fetch customer by id", description = "Returns a single customer from the CIS schema by its id")
	@ApiResponse(responseCode = "200", description = "Customer retrieved successfully", content = @Content(schema = @Schema(additionalProperties = Schema.AdditionalPropertiesValue.TRUE, example = "{\"customer_id\": 1, \"name\": \"Jane Doe\"}")))
	@ApiResponse(responseCode = "404", description = "Customer not found")
	public Map<String, Object> fetchCustomerById(@PathVariable Long id) {
		logger.info("Fetch customer by id requested: id={}", id);
		List<Map<String, Object>> results = jdbcTemplate.queryForList(
				"SELECT * FROM cis.customer WHERE customer_id = ?", id);
		if (results.isEmpty()) {
			throw new CustomerNotFoundException(id);
		}
		return results.get(0);
	}

}
