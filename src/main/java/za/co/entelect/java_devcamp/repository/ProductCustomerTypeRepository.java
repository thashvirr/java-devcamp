package za.co.entelect.java_devcamp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import za.co.entelect.java_devcamp.entity.ProductCustomerType;
import za.co.entelect.java_devcamp.entity.ProductCustomerType.ProductCustomerTypeId;

public interface ProductCustomerTypeRepository extends JpaRepository<ProductCustomerType, ProductCustomerTypeId> {

	boolean existsByProductIdAndCustomerTypesId(Long productId, Long customerTypesId);

}
