package com.tpg.bookshop.services;

import com.tpg.bookshop.web.model.CustomerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static com.tpg.bookshop.services.BookUuids.NOT_FOUND_UUID;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@Service
public class InMemoryCustomerQueryHandler implements CustomerQueryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryCustomerQueryHandler.class);

    @Override
    public Optional<CustomerDto> findByUuid(UUID uuid) {
        if (NOT_FOUND_UUID.equals(uuid)) {
            LOGGER.error("Customer with uuid {} not found", uuid);
            return empty(); }

        LOGGER.info("Found customer with uuid {}", uuid);

        return of(CustomerDto.builder().uuid(uuid).build());
    }
}
