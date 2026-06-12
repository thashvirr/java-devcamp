package za.co.entelect.java_devcamp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import za.co.entelect.java_devcamp.entity.Product;
import za.co.entelect.java_devcamp.service.ProductService;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Products", description = "Product catalogue endpoints")
public class ProductController {

	private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/fetchProducts")
	@Operation(summary = "Fetch all products", description = "Returns every product in the catalogue")
	@ApiResponse(responseCode = "200", description = "Products retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class))))
	public List<Product> fetchProducts() {
		logger.info("Fetch all products requested");
		return productService.fetchAllProducts();
	}

	@GetMapping("/fetchProduct/{id}")
	@Operation(summary = "Fetch product by id", description = "Returns a single product from the catalogue by its id")
	@ApiResponse(responseCode = "200", description = "Product retrieved successfully", content = @Content(schema = @Schema(implementation = Product.class)))
	@ApiResponse(responseCode = "404", description = "Product not found")
	public Product fetchProductById(@PathVariable Long id) {
		logger.info("Fetch product by id requested: id={}", id);
		return productService.fetchProductById(id);
	}

}
