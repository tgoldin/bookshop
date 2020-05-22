package com.tpg.bookshop.services;

import com.tpg.bookshop.web.model.CustomerDto;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.tpg.bookshop.services.BookUuids.NOT_FOUND_UUID;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
public class InMemoryCustomerQueryRequestHandler implements CustomerQueryService {
    @Override
    public Optional<CustomerDto> findByUuid(UUID uuid) {
        if (NOT_FOUND_UUID.equals(uuid)) { return empty(); }

        return of(CustomerDto.builder().uuid(uuid).build());
    }
}
