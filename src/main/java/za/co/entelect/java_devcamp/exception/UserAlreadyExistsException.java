package za.co.entelect.java_devcamp.exception;

public class UserAlreadyExistsException extends RuntimeException {

	public UserAlreadyExistsException(String email) {
		super("User with matching email already exists");
	}

}
