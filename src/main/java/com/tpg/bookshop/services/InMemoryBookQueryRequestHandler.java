package com.tpg.bookshop.services;

import com.tpg.bookshop.web.model.BookDto;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class InMemoryBookQueryRequestHandler implements BookQueryService {
    @Override
    public Optional<BookDto> findByUuid(UUID uuid) {
        return Optional.of(BookDto.builder().uuid(uuid).build());
    }
}
