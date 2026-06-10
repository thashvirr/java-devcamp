package za.co.entelect.java_devcamp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {

	private final JdbcTemplate jdbcTemplate;

	public CustomerController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@GetMapping("/fetchCustomers")
	public List<Map<String, Object>> fetchCustomers() {
		return jdbcTemplate.queryForList("SELECT * FROM cis.customer");
	}

}
