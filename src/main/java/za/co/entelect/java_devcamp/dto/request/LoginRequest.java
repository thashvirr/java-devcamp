package za.co.entelect.java_devcamp.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login credentials")
public class LoginRequest {

	@Schema(example = "products@entelect.co.za")
	private String email;

	@Schema(example = "SpringProducts01$")
	private String password;

	public LoginRequest() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
