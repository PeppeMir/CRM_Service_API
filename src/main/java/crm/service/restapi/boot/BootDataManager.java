package crm.service.restapi.boot;

import crm.service.restapi.model.Role;
import crm.service.restapi.model.User;
import crm.service.restapi.repository.RoleRepository;
import crm.service.restapi.repository.UserRepository;
import crm.service.restapi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
public class BootDataManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${datasource.startup.adminUser.name}")
    private String name;

    @Value("${datasource.startup.adminUser.surname}")
    private String surname;

    @Value("${datasource.startup.adminUser.email}")
    private String email;

    @Value("${datasource.startup.adminUser.password}")
    private String password;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void afterStartup() {

        // always add roles, if not present
        createRoleIfNotPresent(Role.USER);
        final Role adminRole = createRoleIfNotPresent(Role.ADMIN);

        // check whether to add the user or not: skip if at least a field is not specified
        if (areSettingsValid()) {
            createUserIfNotPresent(adminRole);
        }
    }

    private boolean areSettingsValid() {
        return !StringUtils.isEmpty(name) &&
                !StringUtils.isEmpty(surname) &&
                !StringUtils.isEmpty(email) &&
                !StringUtils.isEmpty(password);
    }

    private void createUserIfNotPresent(final Role role) {

        final Optional<User> optUser = userRepository.findByEmail(email);
        if (!optUser.isPresent()) {

            logger.debug("User '{}' not found: creating...", email);

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

            logger.debug("Role '{}' not found: creating...", rolename);

            final Role role = new Role();
            role.setName(rolename);

            roleRepository.save(role);

            logger.debug("Role created: {}", role);

            return role;
        });
    }
}
