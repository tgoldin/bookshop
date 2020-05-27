package com.tpg.bookshop.services.exceptions;

import com.tpg.bookshop.web.model.CustomerDto;

public class FailedToUpdateCustomerException extends Exception {
    public FailedToUpdateCustomerException(CustomerDto customerDto) {
        super(String.format("Failed to update customer with UUID %s.", customerDto.getUuid()));
    }
}
