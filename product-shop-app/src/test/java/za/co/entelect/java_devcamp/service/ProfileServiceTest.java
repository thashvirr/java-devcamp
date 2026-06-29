package za.co.entelect.java_devcamp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import za.co.entelect.java_devcamp.entity.ApplicationUser;
import za.co.entelect.java_devcamp.exception.UserNotFoundException;
import za.co.entelect.java_devcamp.repository.ApplicationUserRepository;

@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

	@Mock
	private ApplicationUserRepository applicationUserRepository;

	@Mock
	private JdbcTemplate jdbcTemplate;

	@InjectMocks
	private ProfileService profileService;

	@Test
	void loadProfile_returnsUserAndCustomerDetails() {
		ApplicationUser user = new ApplicationUser();
		user.setUserId(1L);
		user.setEmail("user@example.com");
		user.setRole("user");

		Map<String, Object> customer = Map.of(
				"customer_id", 10L,
				"email", "user@example.com",
				"first_name", "Test");

		when(applicationUserRepository.findFirstByEmailIgnoreCase("user@example.com")).thenReturn(Optional.of(user));
		when(jdbcTemplate.queryForList(anyString(), eq("user@example.com"))).thenReturn(List.of(customer));

		Map<String, Object> profile = profileService.loadProfile("user@example.com");

		assertThat(profile.get("user_id")).isEqualTo(1L);
		assertThat(profile.get("email")).isEqualTo("user@example.com");
		assertThat(profile.get("role")).isEqualTo("user");
		assertThat(profile.get("customer")).isEqualTo(customer);
	}

	@Test
	void loadProfile_returnsUserWithoutCustomerWhenNoCustomerRecord() {
		ApplicationUser user = new ApplicationUser();
		user.setUserId(2L);
		user.setEmail("orphan@example.com");
		user.setRole("user");

		when(applicationUserRepository.findFirstByEmailIgnoreCase("orphan@example.com")).thenReturn(Optional.of(user));
		when(jdbcTemplate.queryForList(anyString(), eq("orphan@example.com"))).thenReturn(List.of());

		Map<String, Object> profile = profileService.loadProfile("orphan@example.com");

		assertThat(profile.get("email")).isEqualTo("orphan@example.com");
		assertThat(profile).doesNotContainKey("customer");
	}

	@Test
	void loadProfile_throwsWhenUserNotFound() {
		when(applicationUserRepository.findFirstByEmailIgnoreCase("missing@example.com")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> profileService.loadProfile("missing@example.com"))
				.isInstanceOf(UserNotFoundException.class);
	}

}
