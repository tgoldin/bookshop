package com.tpg.bookshop.web.controllers;

import org.springframework.http.HttpHeaders;

import java.util.UUID;

public interface HttpHeadersBuilder {
    String HEADER_LOCATION_KEY = "Location";

    default HttpHeaders generateHttpHeaders(String uri, UUID uuid) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (uuid == null) { return httpHeaders; }

        httpHeaders.add(HEADER_LOCATION_KEY, String.format("%s/%s", uri, uuid));

        return httpHeaders;
    }
}
