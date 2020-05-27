package com.tpg.bookshop.services;

import com.tpg.bookshop.services.exceptions.CannotUpdateNewCustomerException;
import com.tpg.bookshop.services.exceptions.CustomerAlreadyExistsException;
import com.tpg.bookshop.services.exceptions.FailedToSaveCustomerException;
import com.tpg.bookshop.services.exceptions.FailedToUpdateCustomerException;
import com.tpg.bookshop.web.model.CustomerDto;
import com.tpg.bookshop.web.model.NewCustomerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.tpg.bookshop.services.BookUuids.NOT_FOUND_UUID;

@Service
public class InMemoryCustomerCommandHandler implements CustomerCommandService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryCustomerCommandHandler.class);

    private final Map<UUID, CustomerDto> customersByUuid = new ConcurrentHashMap<>();

    @Override
    public CustomerDto createCustomer(NewCustomerRequest request) throws FailedToSaveCustomerException, CustomerAlreadyExistsException {
        simulateFailureToCreateCustomer(request);

        simulateCustomerAlreadyExists(request);

        CustomerDto customerDto = CustomerDto.builder()
                .firstName(request.getFirstName())
                .surname(request.getSurname())
                .uuid(UUID.randomUUID()).build();

        customersByUuid.put(customerDto.getUuid(), customerDto);

        LOGGER.info("Created customer {}", customerDto.getUuid());

        return customerDto;
    }

    private void simulateFailureToCreateCustomer(NewCustomerRequest request) throws FailedToSaveCustomerException {
        if (NOT_FOUND_UUID.toString().equals(request.getFirstName())) {
            LOGGER.error("Simulating failure to update customer {}", NOT_FOUND_UUID);
            throw new FailedToSaveCustomerException("Failed to save new customer.");
        }
    }

    private void simulateCustomerAlreadyExists(NewCustomerRequest request) throws CustomerAlreadyExistsException {
        if (NOT_FOUND_UUID.toString().equals(request.getSurname())) {
            LOGGER.error("Simulating customer {} already exists", NOT_FOUND_UUID);
            throw new CustomerAlreadyExistsException(NOT_FOUND_UUID);
        }
    }

    @Override
    public CustomerDto updateCustomer(CustomerDto customerDto) throws CannotUpdateNewCustomerException, FailedToUpdateCustomerException {
        if (customerDto.getUuid() == null) { throw new CannotUpdateNewCustomerException(); }

        if ((customerDto.getUuid() != null) && (customerDto.getUuid().toString().equals(customerDto.getSurname()))){
            LOGGER.error("Simulating failure to update customer {}", customerDto.getUuid());
            throw new FailedToUpdateCustomerException(customerDto);
        }

        customersByUuid.put(customerDto.getUuid(), customerDto);

        LOGGER.info("Customer with UUID {} updated.", customerDto.getUuid());

        return customerDto;
    }
}
