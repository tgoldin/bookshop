package com.tpg.bookshop.services;

import com.tpg.bookshop.services.exceptions.BookAlreadyExistsException;
import com.tpg.bookshop.services.exceptions.FailedToSaveBookException;
import com.tpg.bookshop.web.model.BookDto;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.tpg.bookshop.services.BookUuids.NOT_FOUND_UUID;

@Service
public class InMemoryBookCommandHandler implements BookCommandService {

    private final Map<UUID, BookDto> booksByUuid = new ConcurrentHashMap<>();

    @Override
    public BookDto createBook(BookDto bookDto) throws FailedToSaveBookException, BookAlreadyExistsException {
        if (NOT_FOUND_UUID.equals(bookDto.getUuid())) { throw new FailedToSaveBookException("Failed to save new book"); }

        if (bookDto.getUuid() != null) { throw new BookAlreadyExistsException(bookDto.getUuid()); }

        bookDto.setUuid(UUID.randomUUID());

        booksByUuid.put(bookDto.getUuid(), bookDto);

        return bookDto;
    }
}
