package za.co.entelect.java_devcamp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import za.co.entelect.java_devcamp.entity.ApplicationUser;
import za.co.entelect.java_devcamp.repository.ApplicationUserRepository;

@ExtendWith(MockitoExtension.class)
class ApplicationUserDetailsServiceTest {

	@Mock
	private ApplicationUserRepository applicationUserRepository;

	@InjectMocks
	private ApplicationUserDetailsService applicationUserDetailsService;

	@Test
	void loadUserByUsername_returnsUserDetailsWhenFound() {
		ApplicationUser user = new ApplicationUser();
		user.setEmail("user@example.com");
		user.setPassword("encoded-password");
		user.setRole("user");

		when(applicationUserRepository.findFirstByEmailIgnoreCase("user@example.com")).thenReturn(Optional.of(user));

		UserDetails userDetails = applicationUserDetailsService.loadUserByUsername("user@example.com");

		assertThat(userDetails.getUsername()).isEqualTo("user@example.com");
		assertThat(userDetails.getPassword()).isEqualTo("encoded-password");
		assertThat(userDetails.getAuthorities()).extracting("authority").containsExactly("ROLE_user");
	}

	@Test
	void loadUserByUsername_throwsWhenNotFound() {
		when(applicationUserRepository.findFirstByEmailIgnoreCase("missing@example.com")).thenReturn(Optional.empty());

		assertThatThrownBy(() -> applicationUserDetailsService.loadUserByUsername("missing@example.com"))
				.isInstanceOf(UsernameNotFoundException.class)
				.hasMessage("User not found: missing@example.com");
	}

}
