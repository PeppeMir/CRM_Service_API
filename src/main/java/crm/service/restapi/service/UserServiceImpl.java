package crm.service.restapi.service;

import crm.service.restapi.exception.GenericErrorException;
import crm.service.restapi.exception.MissingFieldsException;
import crm.service.restapi.exception.ResourceNotFoundException;
import crm.service.restapi.model.Role;
import crm.service.restapi.model.User;
import crm.service.restapi.repository.RoleRepository;
import crm.service.restapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<User> findAll() {

        logger.info("Finding all the users");

        return userRepository.findByActive(true);
    }

    @Override
    public Optional<User> findById(final long userId) {

        logger.info("Finding user \"{}\"", userId);

        final Optional<User> user = userRepository.findByIdAndActive(userId, true);
        if (user.isPresent()) {
            logger.info("User successfully found: {}", user);
        } else {
            logger.warn("User \"{}\" not found", userId);
        }

        return user;
    }

    @Override
    public Optional<User> findByEmail(final String email) {

        logger.info("Finding user with email \"{}\"", email);

        return userRepository.findByEmailAndActive(email, true);
    }

    @Override
    public User create(final User user) {

        // user already present, also if not soft deleted
        final Optional<User> userFromDb = userRepository.findByEmail(user.getEmail());
        if (userFromDb.isPresent()) {
            final User existingUser = userFromDb.get();

            if (existingUser.getActive()) {
                throw new GenericErrorException("User \"" + user.getEmail() + "\" already present");
            } else {
                // ... if not active, reactivate and update it
                existingUser.setActive(true);
                return update(existingUser, user);
            }
        }

        // no specified role
        if (user.getRole() == null) {
            logger.error("No role specified for the creation of the user");
            throw new MissingFieldsException("role");
        }

        final String rolename = user.getRole().getName();

        // invalid specified role
        final Optional<Role> role = roleRepository.findByName(rolename);
        if (!role.isPresent()) {
            logger.error("Invalid role \"{}\" specified for the creation of the user", rolename);
            throw new ResourceNotFoundException("Role", "name", rolename);
        }

        user.setRole(role.get());

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        user.setActive(true);

        userRepository.save(user);

        logger.info("User successfully created: {}", user);

        return user;
    }

    @Override
    public User update(final long userId, final User userUpdates) {

        final User user = findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return update(user, userUpdates);
    }

    private User update(final User user, final User userUpdates) {

        if (!StringUtils.isEmpty(userUpdates.getName())) {
            user.setName(userUpdates.getName());
        }

        if (!StringUtils.isEmpty(userUpdates.getSurname())) {
            user.setSurname(userUpdates.getSurname());
        }

        if (!StringUtils.isEmpty(userUpdates.getEmail())) {
            user.setEmail(userUpdates.getEmail());
        }

        if (!StringUtils.isEmpty(userUpdates.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(userUpdates.getPassword()));
        }

        if (userUpdates.getRole() != null) {
            final String rolename = userUpdates.getRole().getName();
            final Optional<Role> role = roleRepository.findByName(rolename);
            if (!role.isPresent()) {
                logger.error("Invalid role \"{}\" specified for the user update", rolename);
                throw new ResourceNotFoundException("Role", "name", rolename);
            }

            user.setRole(role.get());
        }

        logger.info("Saving updated user {}", user);

        userRepository.save(user);

        return user;
    }

        @Override
    public void delete(final long userId) {

        logger.info("Deleting user \"{}\"", userId);

        final User user = findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // soft delete
        user.setActive(false);

        userRepository.save(user);

        logger.info("User \"{}\" successfully deleted", userId);
    }
}
