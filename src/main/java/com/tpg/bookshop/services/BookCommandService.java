package com.tpg.bookshop.services;

import com.tpg.bookshop.services.exceptions.*;
import com.tpg.bookshop.web.model.BookDto;

public interface BookCommandService {
    BookDto createBook(BookDto bookDto) throws FailedToSaveBookException, BookAlreadyExistsException, MalformedBookRequestException;

    BookDto updateBook(BookDto existingBookDto) throws CannotUpdateNewBookException, FailedToUpdateBookException;
}
