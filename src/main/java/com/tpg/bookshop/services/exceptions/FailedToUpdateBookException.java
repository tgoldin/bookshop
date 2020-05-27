package com.tpg.bookshop.services.exceptions;

import com.tpg.bookshop.web.model.BookDto;

public class FailedToUpdateBookException extends Exception {
    public FailedToUpdateBookException(BookDto bookDto) {
        super(String.format("Failed to update book with UUID %s.", bookDto.getUuid()));
    }
}
