package com.tpg.bookshop.services.exceptions;

public class CannotUpdateNewCustomerException extends Exception {
    public CannotUpdateNewCustomerException() {
        super("Cannot update a new customer.");
    }
}
