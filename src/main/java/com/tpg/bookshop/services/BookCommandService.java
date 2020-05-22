package com.tpg.bookshop.services;

import com.tpg.bookshop.services.exceptions.BookAlreadyExistsException;
import com.tpg.bookshop.services.exceptions.CannotUpdateNewBookException;
import com.tpg.bookshop.services.exceptions.FailedToSaveBookException;
import com.tpg.bookshop.web.model.BookDto;

public interface BookCommandService {
    BookDto createBook(BookDto bookDto) throws FailedToSaveBookException, BookAlreadyExistsException;

    BookDto updateBook(BookDto existingBookDto) throws CannotUpdateNewBookException;
}
