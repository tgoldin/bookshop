package com.tpg.bookshop.web.controller;

import com.tpg.bookshop.services.CustomerQueryService;
import com.tpg.bookshop.web.model.CustomerDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("customers")
public class CustomerController {
    private final CustomerQueryService customerQueryService;

    public CustomerController(CustomerQueryService customerQueryService) {
        this.customerQueryService = customerQueryService;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<CustomerDto> findByUuid(@PathVariable("uuid") UUID uuid) {
        return customerQueryService.findByUuid(uuid).map(c -> new ResponseEntity<>(c, OK)).orElse(null);
    }
}
