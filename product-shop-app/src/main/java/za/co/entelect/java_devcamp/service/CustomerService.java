package za.co.entelect.java_devcamp.service;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import za.co.entelect.java_devcamp.constant.CustomerType;
import za.co.entelect.java_devcamp.dto.request.CreateCustomerRequest;
import za.co.entelect.java_devcamp.entity.ApplicationUser;
import za.co.entelect.java_devcamp.exception.CustomerNotFoundException;
import za.co.entelect.java_devcamp.exception.UserAlreadyExistsException;
import za.co.entelect.java_devcamp.repository.ApplicationUserRepository;

@Service
public class CustomerService {

	private final JdbcTemplate jdbcTemplate;
	private final ApplicationUserRepository applicationUserRepository;
	private final PasswordEncoder passwordEncoder;

	public CustomerService(
			JdbcTemplate jdbcTemplate,
			ApplicationUserRepository applicationUserRepository,
			PasswordEncoder passwordEncoder) {
		this.jdbcTemplate = jdbcTemplate;
		this.applicationUserRepository = applicationUserRepository;
		this.passwordEncoder = passwordEncoder;
	}

	public List<Map<String, Object>> fetchAllCustomers() {
		return jdbcTemplate.queryForList("SELECT * FROM cis.customer");
	}

	public List<Map<String, Object>> fetchCustomersForAuthenticatedUser(String email) {
		Map<String, Object> authenticatedCustomer = fetchCustomerByEmail(email);
		Object customerTypesId = authenticatedCustomer.get("customer_types_id");
		if (customerTypesId instanceof Number number && number.longValue() == CustomerType.ADMIN) {
			return fetchAllCustomers();
		}
		return List.of(authenticatedCustomer);
	}

	public Map<String, Object> fetchCustomerById(Long id) {
		List<Map<String, Object>> results = jdbcTemplate.queryForList(
				"SELECT * FROM cis.customer WHERE customer_id = ?", id);
		if (results.isEmpty()) {
			throw new CustomerNotFoundException(id);
		}
		return results.get(0);
	}

	public Map<String, Object> fetchCustomerByEmail(String email) {
		List<Map<String, Object>> results = jdbcTemplate.queryForList(
				"SELECT * FROM cis.customer WHERE LOWER(email) = LOWER(?)", email);
		if (results.isEmpty()) {
			throw new CustomerNotFoundException(email);
		}
		return results.get(0);
	}

	@Transactional
	public Map<String, Object> createCustomer(CreateCustomerRequest request) {
		if (applicationUserRepository.findFirstByEmailIgnoreCase(request.getEmail()).isPresent()) {
			throw new UserAlreadyExistsException(request.getEmail());
		}

		Map<String, Object> createdCustomer = jdbcTemplate.queryForMap(
				"""
				INSERT INTO cis.customer
				(
				    customer_id,
				    email,
				    first_name,
				    id_number,
				    last_name,
				    customer_types_id
				)
				VALUES (
				    (SELECT COALESCE(MAX(customer_id), 0) + 1 FROM cis.customer),
				    ?, ?, ?, ?, ?
				)
				RETURNING *
				""",
				request.getEmail(),
				request.getFirstName(),
				request.getIdNumber(),
				request.getLastName(),
				1L);

		ApplicationUser applicationUser = new ApplicationUser();
		applicationUser.setEmail(request.getEmail());
		applicationUser.setPassword(passwordEncoder.encode(request.getPassword()));
		applicationUser.setRole("user");
		applicationUserRepository.save(applicationUser);

		return createdCustomer;
	}

}
