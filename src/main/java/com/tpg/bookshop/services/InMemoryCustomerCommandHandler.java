package com.tpg.bookshop.services;

import com.tpg.bookshop.services.exceptions.CustomerAlreadyExistsException;
import com.tpg.bookshop.services.exceptions.FailedToSaveCustomerException;
import com.tpg.bookshop.web.model.CustomerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.tpg.bookshop.services.BookUuids.NOT_FOUND_UUID;

@Service
public class InMemoryCustomerCommandHandler implements CustomerCommandService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryCustomerCommandHandler.class);

    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) throws FailedToSaveCustomerException, CustomerAlreadyExistsException {
        if (NOT_FOUND_UUID.equals(customerDto.getUuid())) {
            LOGGER.error("failed to save new customer");
            throw new FailedToSaveCustomerException("Failed to save new customer."); }

        if (customerDto.getUuid() != null) {
            LOGGER.error("Customer {} already exists", customerDto.getUuid());
            throw new CustomerAlreadyExistsException(customerDto.getUuid()); }

        customerDto.setUuid(UUID.randomUUID());

        LOGGER.info("Created customer {}", customerDto.getUuid());

        return customerDto;
    }
}
