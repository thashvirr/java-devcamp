package za.co.entelect.java_devcamp.service;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import za.co.entelect.java_devcamp.entity.Customer;
import za.co.entelect.java_devcamp.exception.CustomerNotFoundException;

@Service
public class CustomerService {

	private final JdbcTemplate jdbcTemplate;

	public CustomerService(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<Map<String, Object>> fetchAllCustomers() {
		return jdbcTemplate.queryForList("SELECT * FROM cis.customer");
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

	public Map<String, Object> createCustomer(Customer customer) {
		return jdbcTemplate.queryForMap(
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
				customer.getEmail(),
				customer.getFirstName(),
				customer.getIdNumber(),
				customer.getLastName(),
				1L);
	}

}
