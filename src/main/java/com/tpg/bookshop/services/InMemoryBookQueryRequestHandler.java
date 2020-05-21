package com.tpg.bookshop.services;

import com.tpg.bookshop.web.model.BookDto;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
public class InMemoryBookQueryRequestHandler extends InMemoryBasedQueryRequestHandler implements BookQueryService {

    @Override
    public Optional<BookDto> findByUuid(UUID uuid) {
        if (NOT_FOUND_UUID.equals(uuid)) { return empty(); }

        return of(BookDto.builder().uuid(uuid).build());
    }
}
