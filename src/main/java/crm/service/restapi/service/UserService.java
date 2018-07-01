package crm.service.restapi.service;

import crm.service.restapi.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> findById(long userId);

    Optional<User> findByEmail(String email);

    User create(User user);

    User update(long userId, User userUpdates);

    void delete(long userId);
}
