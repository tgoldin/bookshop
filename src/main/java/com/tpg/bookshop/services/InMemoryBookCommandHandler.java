package com.tpg.bookshop.services;

import com.tpg.bookshop.services.exceptions.BookAlreadyExistsException;
import com.tpg.bookshop.services.exceptions.FailedToSaveBookException;
import com.tpg.bookshop.web.model.BookDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.tpg.bookshop.services.BookUuids.NOT_FOUND_UUID;

@Service
public class InMemoryBookCommandHandler implements BookCommandService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryBookCommandHandler.class);

    @Override
    public BookDto createBook(BookDto bookDto) throws FailedToSaveBookException, BookAlreadyExistsException {
        if (NOT_FOUND_UUID.equals(bookDto.getUuid())) {
            LOGGER.error("Failed to save new book");
            throw new FailedToSaveBookException("Failed to save new book");
        }

        if (bookDto.getUuid() != null) {
            LOGGER.error("Book {} already exists", bookDto.getUuid());
            throw new BookAlreadyExistsException(bookDto.getUuid()); }

        bookDto.setUuid(UUID.randomUUID());

        LOGGER.info("Book created {}", bookDto.getUuid());

        return bookDto;
    }
}
