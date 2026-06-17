package za.co.entelect.java_devcamp.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import za.co.entelect.java_devcamp.entity.ApplicationUser;
import za.co.entelect.java_devcamp.repository.ApplicationUserRepository;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

	private final ApplicationUserRepository applicationUserRepository;

	public ApplicationUserDetailsService(ApplicationUserRepository applicationUserRepository) {
		this.applicationUserRepository = applicationUserRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ApplicationUser applicationUser = applicationUserRepository.findFirstByEmailIgnoreCase(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

		return User.builder()
				.username(applicationUser.getEmail())
				.password(applicationUser.getPassword())
				.roles(applicationUser.getRole())
				.build();
	}

}
