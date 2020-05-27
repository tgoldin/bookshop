package com.tpg.bookshop.web.controllers;

import com.tpg.bookshop.services.CustomerCommandService;
import com.tpg.bookshop.services.exceptions.CannotUpdateNewCustomerException;
import com.tpg.bookshop.services.exceptions.CustomerAlreadyExistsException;
import com.tpg.bookshop.services.exceptions.FailedToSaveCustomerException;
import com.tpg.bookshop.services.exceptions.FailedToUpdateCustomerException;
import com.tpg.bookshop.web.model.CustomerDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;
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

    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity updateCustomer(@RequestBody CustomerDto customerDto) {
        try {
            CustomerDto updatedCustomer = customerCommandService.updateCustomer(customerDto);

            return new ResponseEntity(String.format("Updated customer with UUID %s.", updatedCustomer.getUuid()),
                    generateHttpHeaders(CUSTOMERS_COMMAND_URI, updatedCustomer.getUuid()), OK);
        }
        catch (CannotUpdateNewCustomerException ce) {
            return new ResponseEntity<>(ce.getMessage(),
                    generateHttpHeaders(CUSTOMERS_COMMAND_URI, null), BAD_REQUEST);
        }
        catch (FailedToUpdateCustomerException e) {
            return new ResponseEntity(e.getMessage(), generateHttpHeaders(CUSTOMERS_COMMAND_URI, null), INTERNAL_SERVER_ERROR);
        }
    }
}
