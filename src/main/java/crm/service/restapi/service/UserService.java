package crm.service.restapi.service;

import crm.service.restapi.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    Optional<User> find(long userId);

    Optional<User> find(String email);

    User create(User user);

    User update(long userId, User userUpdates);

    User delete(long userId);
}
