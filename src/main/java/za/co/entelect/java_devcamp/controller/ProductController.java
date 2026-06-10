package za.co.entelect.java_devcamp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

	private final JdbcTemplate jdbcTemplate;

	public ProductController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@GetMapping("/fetchProducts")
	public List<Map<String, Object>> fetchProducts() {
		return jdbcTemplate.queryForList("SELECT * FROM public.products");
	}

}
