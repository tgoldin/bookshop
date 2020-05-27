package com.tpg.bookshop.services;

import com.tpg.bookshop.services.exceptions.*;
import com.tpg.bookshop.web.model.BookDto;
import com.tpg.bookshop.web.model.NewBookRequest;

public interface BookCommandService {
    BookDto createBook(NewBookRequest request) throws FailedToSaveBookException, BookAlreadyExistsException, MalformedBookRequestException;

    BookDto updateBook(BookDto existingBookDto) throws CannotUpdateNewBookException, FailedToUpdateBookException;
}
