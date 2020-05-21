package com.tpg.bookshop.web.controller;

import com.tpg.bookshop.web.model.CustomerDto;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

public class CustomerController {
    public ResponseEntity<CustomerDto> findByUuid(UUID uuid) {
        return new ResponseEntity<>(CustomerDto.builder().uuid(uuid).build(), OK);
    }
}
