package com.tpg.bookshop.services.exceptions;

import java.util.UUID;

public class BookAlreadyExistsException extends Exception {
    public BookAlreadyExistsException(UUID uuid) {
        super(String.format("Book with UUID %s already exists.", uuid));
    }
}
