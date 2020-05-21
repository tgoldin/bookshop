package com.tpg.bookshop;

import org.junit.jupiter.api.BeforeEach;

public abstract class UUIDBasedTest implements UUIDBuilder {
    private static final String UUID = "431bdbdf-5def-44ce-9940-f67072dc5943";

    protected static final String NOT_FOUND_UUID = "7debcc77-4ddf-41ae-8af6-999bb45014a2";

    protected java.util.UUID uuid;

    @BeforeEach
    public void setUp() {
        uuid = uuid(UUID);
    }
}
