package za.co.entelect.java_devcamp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import za.co.entelect.java_devcamp.entity.ApplicationUser;

public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {

	Optional<ApplicationUser> findFirstByEmailIgnoreCase(String email);

}
