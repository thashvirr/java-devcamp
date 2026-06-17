package za.co.entelect.java_devcamp.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import za.co.entelect.java_devcamp.dto.ProductTakeUpResponse;
import za.co.entelect.java_devcamp.entity.Product;
import za.co.entelect.java_devcamp.exception.ProductNotFoundException;
import za.co.entelect.java_devcamp.repository.ProductCustomerTypeRepository;
import za.co.entelect.java_devcamp.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductCustomerTypeRepository productCustomerTypeRepository;
	private final CustomerService customerService;

	public ProductService(
			ProductRepository productRepository,
			ProductCustomerTypeRepository productCustomerTypeRepository,
			CustomerService customerService) {
		this.productRepository = productRepository;
		this.productCustomerTypeRepository = productCustomerTypeRepository;
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
		Map<String, Object> customer = customerService.fetchCustomerById(customerId);
		Long customerTypesId = extractCustomerTypesId(customer);
		return productRepository.findActiveEligibleForCustomerType(customerTypesId);
	}

	public ProductTakeUpResponse takeUpProduct(String email, Long productId) {
		Map<String, Object> customer = customerService.fetchCustomerByEmail(email);
		Long customerId = ((Number) customer.get("customer_id")).longValue();
		Long customerTypesId = extractCustomerTypesId(customer);

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ProductNotFoundException(productId));

		boolean eligible = Boolean.TRUE.equals(product.getActive())
				&& productCustomerTypeRepository.existsByProductIdAndCustomerTypesId(productId, customerTypesId);

		return new ProductTakeUpResponse(
				customerId,
				(String) customer.get("email"),
				product.getId(),
				product.getName(),
				eligible);
	}

	private Long extractCustomerTypesId(Map<String, Object> customer) {
		Object customerTypesId = customer.get("customer_types_id");
		if (customerTypesId instanceof Number number) {
			return number.longValue();
		}
		throw new IllegalStateException("Customer record is missing customer_types_id");
	}

}
