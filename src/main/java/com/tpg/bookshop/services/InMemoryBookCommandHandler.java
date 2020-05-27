package com.tpg.bookshop.services;

import com.tpg.bookshop.services.exceptions.*;
import com.tpg.bookshop.web.model.BookDto;
import com.tpg.bookshop.web.model.NewBookRequest;
import com.tpg.bookshop.web.model.UpdateBookRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.tpg.bookshop.services.BookUuids.EXISTING_UUID;
import static com.tpg.bookshop.services.BookUuids.NOT_FOUND_UUID;

@Service
public class InMemoryBookCommandHandler implements BookCommandService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryBookCommandHandler.class);

    private final Map<UUID, BookDto> booksByUuid = new ConcurrentHashMap<>();

    @Override
    public BookDto createBook(NewBookRequest request) throws FailedToSaveBookException, BookAlreadyExistsException, MalformedBookRequestException {
        LOGGER.debug("New book request received {}", request);

        simulateFailureToSaveBook(request);

        simulateMalformedBookRequest(request);

        simulateBookAlreadyExists(request);

        BookDto bookDto = BookDto.builder()
                .uuid(UUID.randomUUID())
                .title(request.getTitle())
                .description(request.getDescription())
                .isbn(request.getIsbn())
                .build();

        booksByUuid.put(bookDto.getUuid(), bookDto);

        LOGGER.info("Book created {}", bookDto.getUuid());

        return bookDto;
    }

    private void simulateFailureToSaveBook(NewBookRequest request) throws FailedToSaveBookException {
        if (EXISTING_UUID.toString().equals(request.getTitle())) {
            LOGGER.error("Simulating a failure to save new book");
            throw new FailedToSaveBookException("Failed to save new book");
        }
    }

    private void simulateMalformedBookRequest(NewBookRequest request) throws MalformedBookRequestException {
        if (EXISTING_UUID.toString().equals(request.getIsbn())) {
            LOGGER.error("Simulating malformed book request");
            throw new MalformedBookRequestException();
        }
    }

    private void simulateBookAlreadyExists(NewBookRequest request) throws BookAlreadyExistsException {
        if (EXISTING_UUID.toString().equals(request.getDescription())) {
            LOGGER.error("Book {} already exists", EXISTING_UUID);
            throw new BookAlreadyExistsException(EXISTING_UUID);
        }
    }

    @Override
    public BookDto updateBook(UpdateBookRequest request) throws CannotUpdateNewBookException, FailedToUpdateBookException {
        if (request.getUuid() == null) { throw new CannotUpdateNewBookException(); }

        simulateFailureToUpdateBook(request);

        BookDto found = booksByUuid.getOrDefault(request.getUuid(), BookDto.builder().build());

        if (found.getUuid() == null) {
            found.setUuid(request.getUuid());
            booksByUuid.put(request.getUuid(), found);
        }

        found.setTitle(request.getTitle());
        found.setDescription(request.getDescription());
        found.setIsbn(request.getIsbn());

        LOGGER.info("Book with UUID {} updated.", request.getUuid());

        return found;
    }

    private void simulateFailureToUpdateBook(UpdateBookRequest request) throws FailedToUpdateBookException {
        if (NOT_FOUND_UUID.equals(request.getUuid()) && (NOT_FOUND_UUID.equals(UUID.fromString(request.getDescription())))) {
            LOGGER.error("Simulating a failure to update existing book");
            throw new FailedToUpdateBookException(request.getUuid());
        }
    }
}
