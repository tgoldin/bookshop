package com.tpg.bookshop.services.exceptions;

public class FailedToSaveBookException extends Exception {
    public FailedToSaveBookException(String msg) {
        super(msg);
    }
}
