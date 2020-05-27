package com.tpg.bookshop.services;

import com.tpg.bookshop.services.exceptions.*;
import com.tpg.bookshop.web.model.CustomerDto;
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
    public CustomerDto createCustomer(CustomerDto customerDto) throws FailedToSaveCustomerException, CustomerAlreadyExistsException {
        if (NOT_FOUND_UUID.equals(customerDto.getUuid())) {
            LOGGER.error("Simulating failure to update customer {}", customerDto.getUuid());
            throw new FailedToSaveCustomerException("Failed to save new customer."); }

        if (customerDto.getUuid() != null) {
            LOGGER.error("Simulating customer {} already exists", customerDto.getUuid());
            throw new CustomerAlreadyExistsException(customerDto.getUuid());
        }

        customerDto.setUuid(UUID.randomUUID());

        customersByUuid.put(customerDto.getUuid(), customerDto);

        LOGGER.info("Created customer {}", customerDto.getUuid());

        return customerDto;
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
