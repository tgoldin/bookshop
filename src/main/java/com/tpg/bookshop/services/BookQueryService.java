package com.tpg.bookshop.services;

import com.tpg.bookshop.web.model.BookDto;

import java.util.Optional;
import java.util.UUID;

public interface BookQueryService {
    Optional<BookDto> findByUuid(UUID uuid);
}
