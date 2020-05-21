package com.tpg.bookshop.services;

import com.tpg.bookshop.web.model.CustomerDto;

import java.util.Optional;
import java.util.UUID;

public interface CustomerQueryService {
    Optional<CustomerDto> findByUuid(UUID uuid);
}
