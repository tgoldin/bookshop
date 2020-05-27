package com.tpg.bookshop.services;

import com.tpg.bookshop.services.exceptions.CannotUpdateNewCustomerException;
import com.tpg.bookshop.services.exceptions.CustomerAlreadyExistsException;
import com.tpg.bookshop.services.exceptions.FailedToSaveCustomerException;
import com.tpg.bookshop.services.exceptions.FailedToUpdateCustomerException;
import com.tpg.bookshop.web.model.CustomerDto;
import com.tpg.bookshop.web.model.NewCustomerRequest;
import com.tpg.bookshop.web.model.UpdateCustomerRequest;

public interface CustomerCommandService {
    CustomerDto createCustomer(NewCustomerRequest request) throws FailedToSaveCustomerException, CustomerAlreadyExistsException;

    CustomerDto updateCustomer(UpdateCustomerRequest request) throws CannotUpdateNewCustomerException, FailedToUpdateCustomerException;
}
