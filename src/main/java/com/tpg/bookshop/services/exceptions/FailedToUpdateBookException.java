package com.tpg.bookshop.services.exceptions;

import java.util.UUID;

public class FailedToUpdateBookException extends Exception {
    public FailedToUpdateBookException(UUID uuid) {
        super(String.format("Failed to update book with UUID %s.", uuid));
    }
}
