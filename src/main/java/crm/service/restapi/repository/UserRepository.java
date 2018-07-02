package crm.service.restapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import crm.service.restapi.model.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByActive(Boolean active);

    Optional<User> findByIdAndActive(long id, Boolean active);

    Optional<User> findByEmail(String email);

	Optional<User> findByEmailAndActive(String email, Boolean active);
}
