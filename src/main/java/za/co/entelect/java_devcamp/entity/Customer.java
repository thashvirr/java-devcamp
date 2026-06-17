package za.co.entelect.java_devcamp.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
		description = "Customer create request",
		example = """
				{
				  "email": "jane.doe@example.com",
				  "first_name": "Jane",
				  "last_name": "Doe",
				  "id_number": "9001015800085",
				  "password": "MySecurePassword1$"
				}
				""")
public class Customer {

	@Schema(example = "jane.doe@example.com")
	private String email;

	@JsonProperty("first_name")
	@Schema(example = "Jane")
	private String firstName;

	@JsonProperty("id_number")
	@Schema(example = "9001015800085")
	private String idNumber;

	@JsonProperty("last_name")
	@Schema(example = "Doe")
	private String lastName;

	@Schema(example = "MySecurePassword1$", description = "Password for login credentials")
	private String password;

	public Customer() {
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
