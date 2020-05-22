package com.tpg.bookshop.services;

import com.tpg.bookshop.services.exceptions.CustomerAlreadyExistsException;
import com.tpg.bookshop.services.exceptions.FailedToSaveCustomerException;
import com.tpg.bookshop.web.model.CustomerDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.tpg.bookshop.services.BookUuids.NOT_FOUND_UUID;

@Service
public class InMemoryCustomerCommandHandler implements CustomerCommandService {
    @Override
    public CustomerDto createCustomer(CustomerDto customerDto) throws FailedToSaveCustomerException, CustomerAlreadyExistsException {
        if (NOT_FOUND_UUID.equals(customerDto.getUuid())) { throw new FailedToSaveCustomerException("Failed to save new customer."); }

        if (customerDto.getUuid() != null) { throw new CustomerAlreadyExistsException(customerDto.getUuid()); }

        customerDto.setUuid(UUID.randomUUID());

        return customerDto;
    }
}
