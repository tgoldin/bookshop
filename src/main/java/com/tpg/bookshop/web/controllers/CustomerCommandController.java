package com.tpg.bookshop.web.controllers;

import com.tpg.bookshop.services.CustomerCommandService;
import com.tpg.bookshop.services.exceptions.CustomerAlreadyExistsException;
import com.tpg.bookshop.services.exceptions.FailedToSaveCustomerException;
import com.tpg.bookshop.web.model.CustomerDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/customers")
public class CustomerCommandController implements HttpHeadersBuilder {
    private static final String CUSTOMERS_COMMAND_URI = "/customers";

    private final CustomerCommandService customerCommandService;

    public CustomerCommandController(CustomerCommandService customerCommandService) {
        this.customerCommandService = customerCommandService;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createCustomer(@RequestBody CustomerDto customerDto) {
        try {
            CustomerDto savedDto = customerCommandService.createCustomer(customerDto);
            return new ResponseEntity(String.format("Saved new customer %s.", savedDto.getUuid()),
                    generateHttpHeaders(CUSTOMERS_COMMAND_URI, savedDto.getUuid()), CREATED);
        } catch (FailedToSaveCustomerException | CustomerAlreadyExistsException ce) {
            return new ResponseEntity<>(ce.getMessage(),
                    generateHttpHeaders(CUSTOMERS_COMMAND_URI, null), INTERNAL_SERVER_ERROR);
        }
    }
}
