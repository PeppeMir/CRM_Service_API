package crm.service.restapi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import crm.service.restapi.model.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
}
