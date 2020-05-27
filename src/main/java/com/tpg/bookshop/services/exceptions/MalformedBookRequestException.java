package com.tpg.bookshop.services.exceptions;

public class MalformedBookRequestException extends Exception {
    public MalformedBookRequestException() {
        super("Malformed book request.");
    }
}
