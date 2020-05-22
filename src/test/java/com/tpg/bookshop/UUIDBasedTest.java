package com.tpg.bookshop;

import org.junit.jupiter.api.BeforeEach;

import static com.tpg.bookshop.services.BookUuids.EXISTING_UUID;

public abstract class UUIDBasedTest {
    protected java.util.UUID uuid;

    @BeforeEach
    public void setUp() {
        uuid = EXISTING_UUID;
    }
}
