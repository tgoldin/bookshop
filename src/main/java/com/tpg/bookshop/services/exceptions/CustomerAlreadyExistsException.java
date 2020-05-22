package com.tpg.bookshop.services.exceptions;

import java.util.UUID;

public class CustomerAlreadyExistsException extends Exception {
    public CustomerAlreadyExistsException(UUID uuid) {
        super(String.format("Customer with UUID %s already exists.", uuid));
    }
}
