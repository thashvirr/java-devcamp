package za.co.entelect.java_devcamp.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	private final JwtEncoder jwtEncoder;
	private final long expirySeconds;

	public JwtService(JwtEncoder jwtEncoder, @Value("${jwt.expiry-seconds}") long expirySeconds) {
		this.jwtEncoder = jwtEncoder;
		this.expirySeconds = expirySeconds;
	}

	public String generateToken(Authentication authentication) {
		Instant now = Instant.now();
		JwtClaimsSet claims = JwtClaimsSet.builder()
				.issuer("java-devcamp")
				.issuedAt(now)
				.expiresAt(now.plusSeconds(expirySeconds))
				.subject(authentication.getName())
				.build();

		JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
		return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
	}

	public long getExpirySeconds() {
		return expirySeconds;
	}

}
