package crm.service.restapi.controller;

import crm.service.restapi.exception.ResourceNotFoundException;
import crm.service.restapi.model.User;
import crm.service.restapi.service.UserService;
import crm.service.restapi.service.validation.ParametersValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/user")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {

    @Autowired
    private ParametersValidatorService parametersValidatorService;

    @Autowired
    private UserService userService;

    @GetMapping("/getAll")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping("/create")
    public User createUser(@Valid @RequestBody final User user, final Errors validationErrors) {

        parametersValidatorService.checkValidationResults(validationErrors);

        return userService.create(user);
    }

    @GetMapping("/get/{id}")
    public User getUserById(@PathVariable(value = "id") final Long userId) {
        return userService.find(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    @PatchMapping("/update/{id}")
    public User updateUser(
            @PathVariable(value = "id") final Long userId,
            @RequestBody final User userUpdates) {

        return userService.update(userId, userUpdates);
    }

    @DeleteMapping("/delete/{id}")
    public User deleteUser(@PathVariable(value = "id") final Long userId) {
        return userService.delete(userId);
    }
}
