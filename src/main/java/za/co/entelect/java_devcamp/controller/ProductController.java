package za.co.entelect.java_devcamp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
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

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/fetchProducts")
	@Operation(summary = "Fetch all products", description = "Returns every product in the catalogue")
	@ApiResponse(responseCode = "200", description = "Products retrieved successfully", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class))))
	public List<Product> fetchProducts() {
		return productService.fetchAllProducts();
	}

}
