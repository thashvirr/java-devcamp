package za.co.entelect.java_devcamp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import za.co.entelect.java_devcamp.dto.response.ProductTakeUpResponse;
import za.co.entelect.java_devcamp.entity.Product;
import za.co.entelect.java_devcamp.exception.ProductNotFoundException;
import za.co.entelect.java_devcamp.repository.ProductCustomerTypeRepository;
import za.co.entelect.java_devcamp.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@Mock
	private ProductRepository productRepository;

	@Mock
	private ProductCustomerTypeRepository productCustomerTypeRepository;

	@Mock
	private CustomerService customerService;

	@InjectMocks
	private ProductService productService;

	@Test
	void fetchAllProducts_returnsAllFromRepository() {
		Product product = sampleProduct(1L, "Savings Account", true);
		when(productRepository.findAll()).thenReturn(List.of(product));

		assertThat(productService.fetchAllProducts()).containsExactly(product);
	}

	@Test
	void fetchProductById_returnsProductWhenFound() {
		Product product = sampleProduct(2L, "Credit Card", true);
		when(productRepository.findById(2L)).thenReturn(Optional.of(product));

		assertThat(productService.fetchProductById(2L)).isEqualTo(product);
	}

	@Test
	void fetchProductById_throwsWhenNotFound() {
		when(productRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> productService.fetchProductById(99L))
				.isInstanceOf(ProductNotFoundException.class)
				.hasMessage("Product not found with id: 99");
	}

	@Test
	void fetchEligibleProducts_returnsProductsForCustomerType() {
		Map<String, Object> customer = Map.of("customer_id", 1L, "customer_types_id", 2L);
		List<Product> eligible = List.of(sampleProduct(3L, "Home Loan", true));
		when(customerService.fetchCustomerById(1L)).thenReturn(customer);
		when(productRepository.findActiveEligibleForCustomerType(2L)).thenReturn(eligible);

		assertThat(productService.fetchEligibleProducts(1L)).isEqualTo(eligible);
		verify(productRepository).findActiveEligibleForCustomerType(2L);
	}

	@Test
	void checkTakeUpEligibility_returnsEligibleWhenActiveAndTypeMatches() {
		Map<String, Object> customer = Map.of(
				"customer_id", 10L,
				"email", "user@example.com",
				"customer_types_id", 3L);
		Product product = sampleProduct(5L, "Personal Loan", true);

		when(customerService.fetchCustomerByEmail("user@example.com")).thenReturn(customer);
		when(productRepository.findById(5L)).thenReturn(Optional.of(product));
		when(productCustomerTypeRepository.existsByProductIdAndCustomerTypesId(5L, 3L)).thenReturn(true);

		ProductTakeUpResponse response = productService.checkTakeUpEligibility("user@example.com", 5L);

		assertThat(response.getCustomerId()).isEqualTo(10L);
		assertThat(response.getEmail()).isEqualTo("user@example.com");
		assertThat(response.getProductId()).isEqualTo(5L);
		assertThat(response.getProductName()).isEqualTo("Personal Loan");
		assertThat(response.isEligible()).isTrue();
	}

	@Test
	void checkTakeUpEligibility_returnsNotEligibleWhenProductInactive() {
		Map<String, Object> customer = Map.of(
				"customer_id", 10L,
				"email", "user@example.com",
				"customer_types_id", 3L);
		Product product = sampleProduct(5L, "Personal Loan", false);

		when(customerService.fetchCustomerByEmail("user@example.com")).thenReturn(customer);
		when(productRepository.findById(5L)).thenReturn(Optional.of(product));

		ProductTakeUpResponse response = productService.checkTakeUpEligibility("user@example.com", 5L);

		assertThat(response.isEligible()).isFalse();
	}

	@Test
	void checkTakeUpEligibility_returnsNotEligibleWhenTypeDoesNotMatch() {
		Map<String, Object> customer = Map.of(
				"customer_id", 10L,
				"email", "user@example.com",
				"customer_types_id", 3L);
		Product product = sampleProduct(5L, "Personal Loan", true);

		when(customerService.fetchCustomerByEmail("user@example.com")).thenReturn(customer);
		when(productRepository.findById(5L)).thenReturn(Optional.of(product));
		when(productCustomerTypeRepository.existsByProductIdAndCustomerTypesId(5L, 3L)).thenReturn(false);

		ProductTakeUpResponse response = productService.checkTakeUpEligibility("user@example.com", 5L);

		assertThat(response.isEligible()).isFalse();
	}

	@Test
	void checkTakeUpEligibility_throwsWhenProductNotFound() {
		Map<String, Object> customer = Map.of(
				"customer_id", 10L,
				"email", "user@example.com",
				"customer_types_id", 3L);

		when(customerService.fetchCustomerByEmail("user@example.com")).thenReturn(customer);
		when(productRepository.findById(99L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> productService.checkTakeUpEligibility("user@example.com", 99L))
				.isInstanceOf(ProductNotFoundException.class);
	}

	private static Product sampleProduct(Long id, String name, boolean active) {
		Product product = new Product();
		product.setId(id);
		product.setName(name);
		product.setPrice(BigDecimal.TEN);
		product.setFulfilmentTypeId(1L);
		product.setActive(active);
		return product;
	}

}
