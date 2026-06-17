package za.co.entelect.java_devcamp.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductTakeUpResponse {

	@JsonProperty("customer_id")
	private Long customerId;

	private String email;

	@JsonProperty("product_id")
	private Long productId;

	@JsonProperty("product_name")
	private String productName;

	private boolean eligible;

	public ProductTakeUpResponse(Long customerId, String email, Long productId, String productName, boolean eligible) {
		this.customerId = customerId;
		this.email = email;
		this.productId = productId;
		this.productName = productName;
		this.eligible = eligible;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public String getEmail() {
		return email;
	}

	public Long getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public boolean isEligible() {
		return eligible;
	}

}
