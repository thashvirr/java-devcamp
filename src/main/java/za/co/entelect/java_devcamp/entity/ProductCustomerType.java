package za.co.entelect.java_devcamp.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "product_customer_types", schema = "public")
@IdClass(ProductCustomerType.ProductCustomerTypeId.class)
public class ProductCustomerType {

	@Id
	@Column(name = "product_id")
	private Long productId;

	@Id
	@Column(name = "customer_types_id")
	private Long customerTypesId;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getCustomerTypesId() {
		return customerTypesId;
	}

	public void setCustomerTypesId(Long customerTypesId) {
		this.customerTypesId = customerTypesId;
	}

	public static class ProductCustomerTypeId implements Serializable {

		private Long productId;
		private Long customerTypesId;

		public ProductCustomerTypeId() {
		}

		public ProductCustomerTypeId(Long productId, Long customerTypesId) {
			this.productId = productId;
			this.customerTypesId = customerTypesId;
		}

		@Override
		public boolean equals(Object other) {
			if (this == other) {
				return true;
			}
			if (other == null || getClass() != other.getClass()) {
				return false;
			}
			ProductCustomerTypeId that = (ProductCustomerTypeId) other;
			return Objects.equals(productId, that.productId)
					&& Objects.equals(customerTypesId, that.customerTypesId);
		}

		@Override
		public int hashCode() {
			return Objects.hash(productId, customerTypesId);
		}

	}

}
