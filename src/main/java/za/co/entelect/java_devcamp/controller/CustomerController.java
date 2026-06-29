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

import za.co.entelect.java_devcamp.dto.request.CreateCustomerRequest;
import za.co.entelect.java_devcamp.entity.Product;
import za.co.entelect.java_devcamp.service.CustomerService;
import za.co.entelect.java_devcamp.service.ProductService;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

	private final CustomerService customerService;
	private final ProductService productService;

	public CustomerController(CustomerService customerService, ProductService productService) {
		this.customerService = customerService;
		this.productService = productService;
	}

	@GetMapping
	public List<Map<String, Object>> listCustomers(@AuthenticationPrincipal Jwt jwt) {
		String email = jwt.getSubject();
		logger.info("List customers requested for email={}", email);
		return customerService.fetchCustomersForAuthenticatedUser(email);
	}

	@GetMapping("/{id}")
	public Map<String, Object> getCustomer(@PathVariable Long id) {
		logger.info("Get customer requested: id={}", id);
		return customerService.fetchCustomerById(id);
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Map<String, Object>> createCustomer(@RequestBody CreateCustomerRequest request) {
		logger.info("Creating customer with email={}", request.getEmail());
		Map<String, Object> createdCustomer = customerService.createCustomer(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
	}

	@GetMapping("/{customerId}/eligible-products")
	public List<Product> listEligibleProducts(@PathVariable Long customerId) {
		logger.info("List eligible products requested: customerId={}", customerId);
		return productService.fetchEligibleProducts(customerId);
	}

}
