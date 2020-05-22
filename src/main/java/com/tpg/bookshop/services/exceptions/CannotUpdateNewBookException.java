package com.tpg.bookshop.services.exceptions;

public class CannotUpdateNewBookException extends Exception {
    public CannotUpdateNewBookException() {
        super("Cannot update a new book.");
    }
}
