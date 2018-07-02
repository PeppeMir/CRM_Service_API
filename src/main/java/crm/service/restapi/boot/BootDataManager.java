package crm.service.restapi.boot;

import crm.service.restapi.model.Role;
import crm.service.restapi.model.User;
import crm.service.restapi.repository.RoleRepository;
import crm.service.restapi.repository.UserRepository;
import crm.service.restapi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BootDataManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void afterStartup() {

        final Role adminRole = createRoleIfNotPresent("ADMIN");
        final Role userRole = createRoleIfNotPresent("USER");

        createUserIfNotPresent("user", "user", "user", userRole);
        createUserIfNotPresent("admin", "admin", "admin", adminRole);
    }

    private void createUserIfNotPresent(
            final String name,
            final String email,
            final String password,
            final Role role) {

        final Optional<User> optUser = userRepository.findByEmail(email);
        if (!optUser.isPresent()) {

            logger.debug("User \"{}\" not found: creating...", email);

            final User user = new User();

            user.setName(name);
            user.setSurname(name);
            user.setEmail(email);
            user.setPassword(bCryptPasswordEncoder.encode(password));
            user.setRole(role);
            user.setActive(true);

            userRepository.save(user);

            logger.debug("User created: {}", user);
        }
    }

    private Role createRoleIfNotPresent(final String rolename) {

        return roleRepository.findByName(rolename).orElseGet(() -> {

            logger.debug("Role \"{}\" not found: creating...", rolename);

            final Role role = new Role();
            role.setName(rolename);

            roleRepository.save(role);

            logger.debug("Role created: {}", role);

            return role;
        });
    }
}
