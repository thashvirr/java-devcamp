package za.co.entelect.java_devcamp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import za.co.entelect.java_devcamp.entity.Product;
import za.co.entelect.java_devcamp.exception.ProductNotFoundException;
import za.co.entelect.java_devcamp.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final CustomerService customerService;

	public ProductService(ProductRepository productRepository, CustomerService customerService) {
		this.productRepository = productRepository;
		this.customerService = customerService;
	}

	public List<Product> fetchAllProducts() {
		return productRepository.findAll();
	}

	public Product fetchProductById(Long id) {
		return productRepository.findById(id)
				.orElseThrow(() -> new ProductNotFoundException(id));
	}

	public List<Product> fetchEligibleProducts(Long customerId) {
		customerService.fetchCustomerById(customerId);
		return productRepository.findByActiveTrue();
	}

}
