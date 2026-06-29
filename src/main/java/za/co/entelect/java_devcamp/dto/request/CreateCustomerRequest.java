package za.co.entelect.java_devcamp.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateCustomerRequest {

	private String email;

	@JsonProperty("first_name")
	private String firstName;

	@JsonProperty("id_number")
	private String idNumber;

	@JsonProperty("last_name")
	private String lastName;

	private String password;

	public CreateCustomerRequest() {
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
