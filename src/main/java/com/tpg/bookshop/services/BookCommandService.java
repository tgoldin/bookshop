package com.tpg.bookshop.services;

import com.tpg.bookshop.services.exceptions.FailedToSaveBookException;
import com.tpg.bookshop.web.model.BookDto;

public interface BookCommandService {
    BookDto createBook(BookDto bookDto) throws FailedToSaveBookException;
}
