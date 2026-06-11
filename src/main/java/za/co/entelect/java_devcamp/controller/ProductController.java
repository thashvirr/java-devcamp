package za.co.entelect.java_devcamp.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import za.co.entelect.java_devcamp.entity.Product;
import za.co.entelect.java_devcamp.service.ProductService;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/fetchProducts")
	public List<Product> fetchProducts() {
		return productService.fetchAllProducts();
	}

}
