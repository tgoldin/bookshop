package com.tpg.bookshop.services.exceptions;

import java.util.UUID;

public class FailedToUpdateCustomerException extends Exception {
    public FailedToUpdateCustomerException(UUID uuid) {
        super(String.format("Failed to update customer with UUID %s.", uuid));
    }
}
