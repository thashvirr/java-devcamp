package za.co.entelect.java_devcamp.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import za.co.entelect.java_devcamp.entity.ApplicationUser;
import za.co.entelect.java_devcamp.exception.UserNotFoundException;
import za.co.entelect.java_devcamp.repository.ApplicationUserRepository;

@Service
public class ProfileService {

	private final ApplicationUserRepository applicationUserRepository;
	private final JdbcTemplate jdbcTemplate;

	public ProfileService(ApplicationUserRepository applicationUserRepository, JdbcTemplate jdbcTemplate) {
		this.applicationUserRepository = applicationUserRepository;
		this.jdbcTemplate = jdbcTemplate;
	}

	public Map<String, Object> loadProfile(String email) {
		ApplicationUser user = applicationUserRepository.findFirstByEmailIgnoreCase(email)
				.orElseThrow(() -> new UserNotFoundException(email));

		Map<String, Object> profile = new LinkedHashMap<>();
		profile.put("user_id", user.getUserId());
		profile.put("email", user.getEmail());
		profile.put("role", user.getRole());

		List<Map<String, Object>> customers = jdbcTemplate.queryForList(
				"SELECT * FROM cis.customer WHERE LOWER(email) = LOWER(?)", email);
		if (!customers.isEmpty()) {
			profile.put("customer", customers.get(0));
		}

		return profile;
	}

}
