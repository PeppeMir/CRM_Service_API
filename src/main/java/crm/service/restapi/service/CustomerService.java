package crm.service.restapi.service;

import crm.service.restapi.model.Customer;
import crm.service.restapi.model.Picture;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<Customer> findAll();

    Optional<Customer> findById(long customerId);

    Customer create(Customer customer, Principal principal);

    Customer update(long customerId, Customer customerUpdates, Principal principal);

    Picture uploadPicture(long customerId, MultipartFile picture) throws IOException;

    Picture findPicture(long customerId);

    void delete(long customerId);
}
