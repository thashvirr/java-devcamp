package za.co.entelect.java_devcamp.exception;

public class ProductNotFoundException extends RuntimeException {

	public ProductNotFoundException(Long productId) {
		super("Product not found with id: " + productId);
	}

}
