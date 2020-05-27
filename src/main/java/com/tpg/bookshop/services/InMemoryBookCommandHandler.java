package com.tpg.bookshop.services;

import com.tpg.bookshop.services.exceptions.*;
import com.tpg.bookshop.web.model.BookDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.tpg.bookshop.services.BookUuids.NOT_FOUND_UUID;

@Service
public class InMemoryBookCommandHandler implements BookCommandService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryBookCommandHandler.class);

    private final Map<UUID, BookDto> booksByUuid = new ConcurrentHashMap<>();

    @Override
    public BookDto createBook(BookDto bookDto) throws FailedToSaveBookException, BookAlreadyExistsException, MalformedBookRequestException {
        LOGGER.debug("Book is {}", bookDto);

        simulateFailureToSaveBook(bookDto);

        if ((bookDto.getUuid() != null) && (bookDto.getDescription() != null)) {
            LOGGER.error("Simulating malformed book request");
            throw new MalformedBookRequestException();
        }

        if (bookDto.getUuid() != null) {
            LOGGER.error("Book {} already exists", bookDto.getUuid());
            throw new BookAlreadyExistsException(bookDto.getUuid());
        }

        bookDto.setUuid(UUID.randomUUID());

        booksByUuid.put(bookDto.getUuid(), bookDto);

        LOGGER.info("Book created {}", bookDto.getUuid());

        return bookDto;
    }

    private void simulateFailureToSaveBook(BookDto bookDto) throws FailedToSaveBookException {
        if (NOT_FOUND_UUID.equals(bookDto.getUuid())) {
            LOGGER.error("Simulating a failure to save new book");
            throw new FailedToSaveBookException("Failed to save new book");
        }
    }

    @Override
    public BookDto updateBook(BookDto bookDto) throws CannotUpdateNewBookException, FailedToUpdateBookException {
        if (bookDto.getUuid() == null) { throw new CannotUpdateNewBookException(); }

        simulateFailureToUpdateBook(bookDto);

        booksByUuid.put(bookDto.getUuid(), bookDto);

        LOGGER.info("Book with UUID {} updated.", bookDto.getUuid());

        return bookDto;
    }

    private void simulateFailureToUpdateBook(BookDto bookDto) throws FailedToUpdateBookException {
        if (NOT_FOUND_UUID.equals(bookDto.getUuid()) && (NOT_FOUND_UUID.equals(UUID.fromString(bookDto.getDescription())))) {
            LOGGER.error("Simulating a failure to update existing book");
            throw new FailedToUpdateBookException(bookDto);
        }
    }
}
