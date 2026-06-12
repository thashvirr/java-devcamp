package za.co.entelect.java_devcamp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import za.co.entelect.java_devcamp.entity.Product;
import za.co.entelect.java_devcamp.exception.ProductNotFoundException;
import za.co.entelect.java_devcamp.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<Product> fetchAllProducts() {
		return productRepository.findAll();
	}

	public Product fetchProductById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException(id));
	}

}
