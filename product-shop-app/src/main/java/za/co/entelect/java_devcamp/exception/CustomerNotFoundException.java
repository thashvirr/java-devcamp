package za.co.entelect.java_devcamp.exception;

public class CustomerNotFoundException extends RuntimeException {

	public CustomerNotFoundException(Long customerId) {
		super("Customer not found with customer_id: " + customerId);
	}

	public CustomerNotFoundException(String email) {
		super("Customer not found with email: " + email);
	}

}
