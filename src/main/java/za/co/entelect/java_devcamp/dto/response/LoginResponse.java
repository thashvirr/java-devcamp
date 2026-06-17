package za.co.entelect.java_devcamp.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT login response")
public class LoginResponse {

	@Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
	private String token;

	@Schema(example = "Bearer")
	private String tokenType;

	@Schema(example = "3600")
	private long expiresIn;

	public LoginResponse(String token, String tokenType, long expiresIn) {
		this.token = token;
		this.tokenType = tokenType;
		this.expiresIn = expiresIn;
	}

	public String getToken() {
		return token;
	}

	public String getTokenType() {
		return tokenType;
	}

	public long getExpiresIn() {
		return expiresIn;
	}

}
