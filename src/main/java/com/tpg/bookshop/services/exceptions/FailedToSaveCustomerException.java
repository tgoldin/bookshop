package com.tpg.bookshop.services.exceptions;

public class FailedToSaveCustomerException extends Exception {
    public FailedToSaveCustomerException(String msg) {
        super(msg);
    }
}
