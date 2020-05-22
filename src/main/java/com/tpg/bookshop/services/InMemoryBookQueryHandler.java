package com.tpg.bookshop.services;

import com.tpg.bookshop.web.model.BookDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.tpg.bookshop.services.BookUuids.NOT_FOUND_UUID;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
public class InMemoryBookQueryHandler implements BookQueryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryBookQueryHandler.class);

    @Override
    public Optional<BookDto> findByUuid(UUID uuid) {
        if (NOT_FOUND_UUID.equals(uuid)) {
            LOGGER.warn("Book {} not found", uuid);
            return empty();
        }

        LOGGER.info("Found book with uuid {}", uuid);

        return of(BookDto.builder().uuid(uuid).build());
    }
}
