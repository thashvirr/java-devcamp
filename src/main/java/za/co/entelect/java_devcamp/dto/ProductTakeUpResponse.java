package za.co.entelect.java_devcamp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Product take-up eligibility result for the logged-in customer")
public class ProductTakeUpResponse {

	@JsonProperty("customer_id")
	@Schema(example = "1")
	private Long customerId;

	@Schema(example = "jane.doe@example.com")
	private String email;

	@JsonProperty("product_id")
	@Schema(example = "2")
	private Long productId;

	@JsonProperty("product_name")
	@Schema(example = "Cheque Account")
	private String productName;

	@Schema(example = "true")
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
