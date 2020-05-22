package com.tpg.bookshop.services;

import com.tpg.bookshop.services.exceptions.BookAlreadyExistsException;
import com.tpg.bookshop.services.exceptions.FailedToSaveBookException;
import com.tpg.bookshop.web.model.BookDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.tpg.bookshop.services.BookUuids.NOT_FOUND_UUID;

@Service
public class InMemoryBookCommandHandler implements BookCommandService {

    @Override
    public BookDto createBook(BookDto bookDto) throws FailedToSaveBookException, BookAlreadyExistsException {
        if (NOT_FOUND_UUID.equals(bookDto.getUuid())) { throw new FailedToSaveBookException("Failed to save new book"); }

        if (bookDto.getUuid() != null) { throw new BookAlreadyExistsException(bookDto.getUuid()); }

        bookDto.setUuid(UUID.randomUUID());

        return bookDto;
    }
}
