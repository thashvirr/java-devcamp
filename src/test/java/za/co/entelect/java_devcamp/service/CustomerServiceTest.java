package za.co.entelect.java_devcamp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import za.co.entelect.java_devcamp.constant.CustomerType;
import za.co.entelect.java_devcamp.dto.request.CreateCustomerRequest;
import za.co.entelect.java_devcamp.entity.ApplicationUser;
import za.co.entelect.java_devcamp.exception.CustomerNotFoundException;
import za.co.entelect.java_devcamp.exception.UserAlreadyExistsException;
import za.co.entelect.java_devcamp.repository.ApplicationUserRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

	@Mock
	private JdbcTemplate jdbcTemplate;

	@Mock
	private ApplicationUserRepository applicationUserRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private CustomerService customerService;

	@Test
	void fetchCustomerById_returnsCustomerWhenFound() {
		Map<String, Object> customer = Map.of("customer_id", 1L, "email", "a@example.com");
		when(jdbcTemplate.queryForList(anyString(), eq(1L))).thenReturn(List.of(customer));

		assertThat(customerService.fetchCustomerById(1L)).isEqualTo(customer);
	}

	@Test
	void fetchCustomerById_throwsWhenNotFound() {
		when(jdbcTemplate.queryForList(anyString(), eq(99L))).thenReturn(List.of());

		assertThatThrownBy(() -> customerService.fetchCustomerById(99L))
				.isInstanceOf(CustomerNotFoundException.class)
				.hasMessage("Customer not found with customer_id: 99");
	}

	@Test
	void fetchCustomerByEmail_returnsCustomerWhenFound() {
		Map<String, Object> customer = Map.of("customer_id", 2L, "email", "b@example.com");
		when(jdbcTemplate.queryForList(anyString(), eq("b@example.com"))).thenReturn(List.of(customer));

		assertThat(customerService.fetchCustomerByEmail("b@example.com")).isEqualTo(customer);
	}

	@Test
	void fetchCustomerByEmail_throwsWhenNotFound() {
		when(jdbcTemplate.queryForList(anyString(), eq("missing@example.com"))).thenReturn(List.of());

		assertThatThrownBy(() -> customerService.fetchCustomerByEmail("missing@example.com"))
				.isInstanceOf(CustomerNotFoundException.class)
				.hasMessage("Customer not found with email: missing@example.com");
	}

	@Test
	void fetchCustomersForAuthenticatedUser_returnsAllCustomersForAdmin() {
		Map<String, Object> admin = Map.of(
				"customer_id", 1L,
				"email", "admin@example.com",
				"customer_types_id", CustomerType.ADMIN);
		List<Map<String, Object>> allCustomers = List.of(
				admin,
				Map.of("customer_id", 2L, "email", "user@example.com", "customer_types_id", 1L));

		when(jdbcTemplate.queryForList(anyString(), eq("admin@example.com"))).thenReturn(List.of(admin));
		when(jdbcTemplate.queryForList("SELECT * FROM cis.customer")).thenReturn(allCustomers);

		assertThat(customerService.fetchCustomersForAuthenticatedUser("admin@example.com"))
				.isEqualTo(allCustomers);
	}

	@Test
	void fetchCustomersForAuthenticatedUser_returnsOnlySelfForNonAdmin() {
		Map<String, Object> customer = Map.of(
				"customer_id", 2L,
				"email", "user@example.com",
				"customer_types_id", 1L);
		when(jdbcTemplate.queryForList(anyString(), eq("user@example.com"))).thenReturn(List.of(customer));

		assertThat(customerService.fetchCustomersForAuthenticatedUser("user@example.com"))
				.containsExactly(customer);
	}

	@Test
	void createCustomer_persistsCustomerAndApplicationUser() {
		CreateCustomerRequest request = new CreateCustomerRequest();
		request.setEmail("new@example.com");
		request.setFirstName("Jane");
		request.setLastName("Doe");
		request.setIdNumber("9001010000080");
		request.setPassword("secret");

		Map<String, Object> createdCustomer = Map.of(
				"customer_id", 3L,
				"email", "new@example.com",
				"first_name", "Jane",
				"last_name", "Doe");

		when(applicationUserRepository.findFirstByEmailIgnoreCase("new@example.com")).thenReturn(Optional.empty());
		when(jdbcTemplate.queryForMap(anyString(), any(), any(), any(), any(), any())).thenReturn(createdCustomer);
		when(passwordEncoder.encode("secret")).thenReturn("encoded-password");

		Map<String, Object> result = customerService.createCustomer(request);

		assertThat(result).isEqualTo(createdCustomer);

		ArgumentCaptor<ApplicationUser> userCaptor = ArgumentCaptor.forClass(ApplicationUser.class);
		verify(applicationUserRepository).save(userCaptor.capture());
		ApplicationUser savedUser = userCaptor.getValue();
		assertThat(savedUser.getEmail()).isEqualTo("new@example.com");
		assertThat(savedUser.getPassword()).isEqualTo("encoded-password");
		assertThat(savedUser.getRole()).isEqualTo("user");
	}

	@Test
	void createCustomer_throwsWhenEmailAlreadyExists() {
		CreateCustomerRequest request = new CreateCustomerRequest();
		request.setEmail("existing@example.com");

		when(applicationUserRepository.findFirstByEmailIgnoreCase("existing@example.com"))
				.thenReturn(Optional.of(new ApplicationUser()));

		assertThatThrownBy(() -> customerService.createCustomer(request))
				.isInstanceOf(UserAlreadyExistsException.class);

		verify(jdbcTemplate, never()).queryForMap(anyString(), any(), any(), any(), any(), any());
	}

}
