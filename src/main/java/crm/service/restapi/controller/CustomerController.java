package crm.service.restapi.controller;

import crm.service.restapi.exception.GenericErrorException;
import crm.service.restapi.exception.ResourceNotFoundException;
import crm.service.restapi.model.Customer;
import crm.service.restapi.model.Picture;
import crm.service.restapi.service.customer.CustomerService;
import crm.service.restapi.service.validation.ParametersValidatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private ParametersValidatorService parametersValidatorService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/getAll")
    public List<Customer> getAllCustomer() {
        return customerService.findAll();
    }

    @PostMapping("/create")
    public Customer createCustomer(
            @Valid @RequestBody final Customer customer,
            final Errors validationErrors,
            final Principal principal) {

        parametersValidatorService.checkValidationResults(validationErrors);

        return customerService.create(customer, principal);
    }

    @GetMapping("/get/{id}")
    public Customer getCustomerById(@PathVariable(value = "id") final Long customerId) {
        return customerService.find(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
    }

    @PatchMapping("/update/{id}")
    public Customer updateCustomer(
            @PathVariable(value = "id") final Long customerId,
            @RequestBody final Customer customerUpdates,
            final Principal principal) {

        return customerService.update(customerId, customerUpdates, principal);
    }

    @PostMapping("/uploadPicture/{id}")
    public Picture uploadCustomerPicture(
            @PathVariable(value = "id") final Long customerId,
            @RequestParam("file") final MultipartFile file) {

        final Picture picture;
        try {
            picture = customerService.uploadPicture(customerId, file);
        } catch (final IOException e) {
            throw new GenericErrorException("An error occurred while uploading the picture");
        }

        return picture;
    }

    @GetMapping(value = "/findPicture/{id}")
    public Picture getCustomerPicture(@PathVariable(value = "id") final Long customerId) {

        return customerService.findPicture(customerId);
    }

    @DeleteMapping("/delete/{id}")
    public Customer deleteCustomer(@PathVariable(value = "id") final Long customerId) {

        return customerService.delete(customerId);
    }
}
