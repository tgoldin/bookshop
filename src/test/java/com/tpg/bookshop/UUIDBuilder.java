package com.tpg.bookshop;

import java.util.UUID;

public interface UUIDBuilder {
    default UUID uuid(String value) { return UUID.fromString(value); }
}
