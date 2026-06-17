package za.co.entelect.java_devcamp.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import za.co.entelect.java_devcamp.dto.request.CreateCustomerRequest;
import za.co.entelect.java_devcamp.entity.Product;
import za.co.entelect.java_devcamp.service.CustomerService;
import za.co.entelect.java_devcamp.service.ProductService;

@RestController
@RequestMapping("/api/v1/customers")
@Tag(name = "Customers", description = "Customer management endpoints")
public class CustomerController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	private final CustomerService customerService;
	private final ProductService productService;

	public CustomerController(CustomerService customerService, ProductService productService) {
		this.customerService = customerService;
		this.productService = productService;
	}

	@GetMapping
	@Operation(
			summary = "List customers",
			description = "Returns the authenticated customer's record, or all customers when the user has admin customer type (ID 5)",
			security = @SecurityRequirement(name = "bearer-jwt"))
	@ApiResponse(responseCode = "200", description = "Customers retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(additionalProperties = Schema.AdditionalPropertiesValue.TRUE, example = "{\"customer_id\": 1, \"name\": \"Jane Doe\"}"))))
	@ApiResponse(responseCode = "401", description = "Not authenticated")
	@ApiResponse(responseCode = "404", description = "Customer not found")
	public List<Map<String, Object>> listCustomers(@AuthenticationPrincipal Jwt jwt) {
		String email = jwt.getSubject();
		logger.info("List customers requested for email={}", email);
		return customerService.fetchCustomersForAuthenticatedUser(email);
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get customer by ID", description = "Returns a single customer from the CIS schema")
	@ApiResponse(responseCode = "200", description = "Customer retrieved successfully", content = @Content(schema = @Schema(additionalProperties = Schema.AdditionalPropertiesValue.TRUE, example = "{\"customer_id\": 1, \"name\": \"Test User\"}")))
	@ApiResponse(responseCode = "404", description = "Customer not found")
	public Map<String, Object> getCustomer(@PathVariable Long id) {
		logger.info("Get customer requested: id={}", id);
		return customerService.fetchCustomerById(id);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(
			summary = "Register a customer",
			description = "Creates a customer in the CIS schema and login credentials in auth.application_user",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					required = true,
					content = @Content(
							mediaType = MediaType.APPLICATION_JSON_VALUE,
							schema = @Schema(implementation = CreateCustomerRequest.class),
							examples = @ExampleObject(
									name = "default",
									value = """
											{
											  "email": "test@user.com",
											  "first_name": "Test",
											  "last_name": "User",
											  "id_number": "9000000000000",
											  "password": "password"
											}
											"""))))
	@ApiResponse(responseCode = "201", description = "Customer created successfully", content = @Content(schema = @Schema(additionalProperties = Schema.AdditionalPropertiesValue.TRUE, example = "{\"customer_id\": 4, \"email\": \"test@user.com\", \"first_name\": \"Test\", \"last_name\": \"User\", \"id_number\": \"9000000000000\", \"customer_types_id\": 1}")))
	@ApiResponse(responseCode = "409", description = "User with matching email already exists")
	public ResponseEntity<Map<String, Object>> createCustomer(@RequestBody CreateCustomerRequest request) {
		logger.info("Creating customer with email={}", request.getEmail());
		Map<String, Object> createdCustomer = customerService.createCustomer(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
	}

	@GetMapping("/{customerId}/eligible-products")
	@Operation(summary = "List eligible products for a customer", description = "Returns products the customer is eligible for based on their customer type")
	@ApiResponse(responseCode = "200", description = "Eligible products retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class))))
	@ApiResponse(responseCode = "404", description = "Customer not found")
	public List<Product> listEligibleProducts(@PathVariable Long customerId) {
		logger.info("List eligible products requested: customerId={}", customerId);
		return productService.fetchEligibleProducts(customerId);
	}

}
