package za.co.entelect.java_devcamp.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import za.co.entelect.java_devcamp.dto.request.LoginRequest;
import za.co.entelect.java_devcamp.dto.response.LoginResponse;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private JwtService jwtService;

	@Mock
	private Authentication authentication;

	@InjectMocks
	private AuthService authService;

	@Test
	void login_returnsTokenResponseOnSuccessfulAuthentication() {
		LoginRequest request = new LoginRequest();
		request.setEmail("user@example.com");
		request.setPassword("password");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authentication);
		when(jwtService.generateToken(authentication)).thenReturn("jwt-token");
		when(jwtService.getExpirySeconds()).thenReturn(3600L);

		LoginResponse response = authService.login(request);

		assertThat(response.getToken()).isEqualTo("jwt-token");
		assertThat(response.getTokenType()).isEqualTo("Bearer");
		assertThat(response.getExpiresIn()).isEqualTo(3600L);

		verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(jwtService).generateToken(authentication);
	}

}
