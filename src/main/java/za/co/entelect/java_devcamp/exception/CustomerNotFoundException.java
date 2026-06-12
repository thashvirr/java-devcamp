package za.co.entelect.java_devcamp.exception;

public class CustomerNotFoundException extends RuntimeException {

	public CustomerNotFoundException(Long customerId) {
		super("Customer not found with customer_id: " + customerId);
	}

}
