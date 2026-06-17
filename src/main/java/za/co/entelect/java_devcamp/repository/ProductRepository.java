package za.co.entelect.java_devcamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import za.co.entelect.java_devcamp.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("""
			SELECT p FROM Product p
			WHERE p.active = true
			AND EXISTS (
				SELECT 1 FROM ProductCustomerType pct
				WHERE pct.productId = p.id AND pct.customerTypesId = :customerTypesId
			)
			""")
	List<Product> findActiveEligibleForCustomerType(@Param("customerTypesId") Long customerTypesId);

}
