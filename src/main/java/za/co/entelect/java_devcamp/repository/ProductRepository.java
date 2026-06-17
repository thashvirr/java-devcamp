package za.co.entelect.java_devcamp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import za.co.entelect.java_devcamp.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	List<Product> findByActiveTrue();

}
