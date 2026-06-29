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

import za.co.entelect.java_devcamp.dto.response.ProductTakeUpResponse;
import za.co.entelect.java_devcamp.entity.Product;
import za.co.entelect.java_devcamp.service.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping
	public List<Product> listProducts() {
		logger.info("List all products requested");
		return productService.fetchAllProducts();
	}

	@GetMapping("/{id}")
	public Product getProduct(@PathVariable Long id) {
		logger.info("Get product requested: id={}", id);
		return productService.fetchProductById(id);
	}

	@PostMapping("/{productId}/take-up")
	public ProductTakeUpResponse checkTakeUpEligibility(
			@AuthenticationPrincipal Jwt jwt,
			@PathVariable Long productId) {
		String email = jwt.getSubject();
		logger.info("Product take-up eligibility check: email={}, productId={}", email, productId);
		return productService.checkTakeUpEligibility(email, productId);
	}

}
