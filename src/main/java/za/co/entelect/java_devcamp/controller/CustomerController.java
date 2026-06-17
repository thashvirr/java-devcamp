package za.co.entelect.java_devcamp.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import za.co.entelect.java_devcamp.entity.Customer;
import za.co.entelect.java_devcamp.service.CustomerService;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Customers", description = "Customer data endpoints")
public class CustomerController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@GetMapping("/customers/get")
	@Operation(summary = "Fetch all customers", description = "Returns every customer from the CIS schema")
	@ApiResponse(responseCode = "200", description = "Customers retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(additionalProperties = Schema.AdditionalPropertiesValue.TRUE, example = "{\"customer_id\": 1, \"name\": \"Jane Doe\"}"))))
	public List<Map<String, Object>> fetchCustomers() {
		logger.info("Fetch all customers requested");
		return customerService.fetchAllCustomers();
	}

	@GetMapping("/customers/get/{id}")
	@Operation(summary = "Fetch customer by id", description = "Returns a single customer from the CIS schema by its id")
	@ApiResponse(responseCode = "200", description = "Customer retrieved successfully", content = @Content(schema = @Schema(additionalProperties = Schema.AdditionalPropertiesValue.TRUE, example = "{\"customer_id\": 1, \"name\": \"Jane Doe\"}")))
	@ApiResponse(responseCode = "404", description = "Customer not found")
	public Map<String, Object> fetchCustomerById(@PathVariable Long id) {
		logger.info("Fetch customer by id requested: id={}", id);
		return customerService.fetchCustomerById(id);
	}

	@PostMapping(value = "/customers/create", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(
			summary = "Create a customer",
			description = "Creates a new customer in the CIS schema and login credentials in auth.application_user",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					required = true,
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = Customer.class),
							examples = @ExampleObject(
									name = "default",
									summary = "New customer",
									value = """
											{
											  "email": "jane.doe@example.com",
											  "first_name": "Jane",
											  "last_name": "Doe",
											  "id_number": "9001015800085",
											  "password": "MySecurePassword1$"
											}
											"""))))
	@ApiResponse(responseCode = "201", description = "Customer created successfully", content = @Content(schema = @Schema(additionalProperties = Schema.AdditionalPropertiesValue.TRUE, example = "{\"customer_id\": 4, \"email\": \"jane.doe@example.com\", \"first_name\": \"Jane\", \"last_name\": \"Doe\", \"id_number\": \"9001015800085\", \"customer_types_id\": 1}")))
	@ApiResponse(responseCode = "409", description = "User with matching email already exists")
	public ResponseEntity<Map<String, Object>> createCustomer(@RequestBody Customer customer) {
		logger.info("Creating customer with email={}", customer.getEmail());
		Map<String, Object> createdCustomer = customerService.createCustomer(customer);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
	}

}
