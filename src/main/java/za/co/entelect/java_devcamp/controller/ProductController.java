package za.co.entelect.java_devcamp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import za.co.entelect.java_devcamp.dto.response.ProductTakeUpResponse;
import za.co.entelect.java_devcamp.entity.Product;
import za.co.entelect.java_devcamp.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Products", description = "Product catalogue endpoints")
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	@Operation(summary = "List all products", description = "Returns every product in the catalogue")
	@ApiResponse(responseCode = "200", description = "Products retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class))))
	public List<Product> listProducts() {
		logger.info("List all products requested");
		return productService.fetchAllProducts();
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get product by ID", description = "Returns a single product from the catalogue")
	@ApiResponse(responseCode = "200", description = "Product retrieved successfully", content = @Content(schema = @Schema(implementation = Product.class)))
	@ApiResponse(responseCode = "404", description = "Product not found")
	public Product getProduct(@PathVariable Long id) {
		logger.info("Get product requested: id={}", id);
		return productService.fetchProductById(id);
	}

	@PostMapping("/{productId}/take-up")
	@Operation(
			summary = "Check product take-up eligibility",
			description = "Returns whether the authenticated customer is eligible to take up the given product",
			security = @SecurityRequirement(name = "bearer-jwt"))
	@ApiResponse(responseCode = "200", description = "Eligibility result returned", content = @Content(schema = @Schema(implementation = ProductTakeUpResponse.class)))
	@ApiResponse(responseCode = "401", description = "Not authenticated")
	@ApiResponse(responseCode = "404", description = "Product or customer not found")
	public ProductTakeUpResponse checkTakeUpEligibility(
			@AuthenticationPrincipal Jwt jwt,
			@PathVariable Long productId) {
		String email = jwt.getSubject();
		logger.info("Product take-up eligibility check: email={}, productId={}", email, productId);
		return productService.checkTakeUpEligibility(email, productId);
	}

}
