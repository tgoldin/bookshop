package com.tpg.bookshop.services;

import com.tpg.bookshop.services.exceptions.CustomerAlreadyExistsException;
import com.tpg.bookshop.services.exceptions.FailedToSaveCustomerException;
import com.tpg.bookshop.web.model.CustomerDto;

public interface CustomerCommandService {
    CustomerDto createCustomer(CustomerDto customerDto) throws FailedToSaveCustomerException, CustomerAlreadyExistsException;
}
