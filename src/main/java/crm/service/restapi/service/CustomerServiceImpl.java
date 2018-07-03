package crm.service.restapi.service;

import crm.service.restapi.exception.GenericErrorException;
import crm.service.restapi.exception.ResourceNotFoundException;
import crm.service.restapi.model.Customer;
import crm.service.restapi.model.Picture;
import crm.service.restapi.model.User;
import crm.service.restapi.repository.CustomerRepository;
import crm.service.restapi.service.picture.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service("customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private UserService userService;

    @Override
    public List<Customer> findAll() {

        logger.info("Finding all the customers");

        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> find(final long customerId) {

        logger.info("Finding customer '{}'", customerId);

        final Customer customer = customerRepository.findOne(customerId);
        if (customer == null) {
            logger.warn("Customer '{}' not found", customerId);
            return Optional.empty();
        }

        logger.info("Customer successfully found: {}", customer);

        return Optional.of(customer);
    }

    @Override
    public Customer create(final Customer customer, final Principal principal) {

        logger.info("Creating customer '{}'", customer);

        final User principalUser = findPrincipalUser(principal);

        customer.setCreationUser(principalUser);
        customer.setLastModUser(principalUser);
        customer.setActive(true);

        customerRepository.save(customer);

        logger.info("Customer successfully created: {}", customer);

        return customer;
    }

    @Override
    public Customer update(final long customerId, final Customer customerUpdates, final Principal principal) {

        logger.info("Updating customer '{}'", customerId);

        final Customer customer = find(customerId).orElseThrow(() ->
                new ResourceNotFoundException("Customer", "id", customerId));

        final User principalUser = findPrincipalUser(principal);

        customer.setLastModUser(principalUser);

        if (!StringUtils.isEmpty(customerUpdates.getName())) {
            customer.setName(customerUpdates.getName());
        }

        if (!StringUtils.isEmpty(customerUpdates.getSurname())) {
            customer.setSurname(customerUpdates.getSurname());
        }

        logger.info("Saving updated customer {}", customer);

        customerRepository.save(customer);

        return customer;
    }

    @Override
    public Picture uploadPicture(final long customerId, final MultipartFile file) {

        logger.info("Uploading picture for customer '{}'", customerId);

        final Customer customer = find(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));

        final Picture picture;
        try {
            picture = pictureService.store(file);

            // delete old picture from the database
            if (customer.getPicture() != null) {
                logger.info("Deleting old picture {}", picture);
                pictureService.delete(customer.getPicture());
            }
        } catch (final IOException e) {
            logger.error("Unable to upload the picture", e);
            throw new GenericErrorException(e.getMessage());
        }

        customer.setPicture(picture);

        logger.info("Updating customer with the new picture {}", customer);

        customerRepository.save(customer);

        return picture;
    }

    @Override
    public Picture findPicture(final long customerId) {

        logger.info("Finding picture for customer '{}'", customerId);

        final Customer customer = find(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));

        if (customer.getPicture() == null) {
            logger.error("Picture not found for customer: {}", customer);
            throw new ResourceNotFoundException("Picture", "customerId", customerId);
        }

        logger.info("Replying with picture found for customer: {}", customer);

        return customer.getPicture();
    }

    @Override
    public Customer delete(final long customerId) {

        logger.info("Deleting customer '{}'", customerId);

        final Customer customer = find(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));

        // soft delete
        customer.setActive(false);

        customerRepository.save(customer);

        logger.info("Customer '{}' successfully deleted", customerId);

        return customer;
    }

    private User findPrincipalUser(final Principal principal) {
        final String principalName = principal.getName();

        logger.info("Looking for principal '{}'", principalName);

        final User principalUser = userService.find(principalName).orElseThrow(() ->
                new GenericErrorException("User has been removed"));

        logger.info("Using user as principal: {}", principalUser);

        return principalUser;
    }
}
