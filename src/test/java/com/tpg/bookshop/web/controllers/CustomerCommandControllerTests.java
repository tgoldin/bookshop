package com.tpg.bookshop.web.controllers;

import com.tpg.bookshop.UUIDBasedTest;
import com.tpg.bookshop.services.CustomerCommandService;
import com.tpg.bookshop.services.exceptions.CannotUpdateNewCustomerException;
import com.tpg.bookshop.services.exceptions.CustomerAlreadyExistsException;
import com.tpg.bookshop.services.exceptions.FailedToSaveCustomerException;
import com.tpg.bookshop.services.exceptions.FailedToUpdateCustomerException;
import com.tpg.bookshop.web.model.CustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
public class CustomerCommandControllerTests extends UUIDBasedTest {
    private CustomerDto newCustomer;

    @Mock
    private CustomerCommandService customerCommandService;

    @InjectMocks
    private CustomerCommandController controller;

    @BeforeEach
    public void setUp() {
        super.setUp();

        newCustomer = CustomerDto.builder().firstName("John").surname("Doe").build();
    }

    @Test
    public void givenANewCustomer_whenPostingNewCustomer_thenCustomerIsCreatedAndCreatedResponseIsReturned() throws FailedToSaveCustomerException, CustomerAlreadyExistsException {
        CustomerDto savedCustomer = CustomerDto.builder().uuid(uuid).firstName("John").surname("Doe").build();

        when(customerCommandService.createCustomer(newCustomer)).thenReturn(savedCustomer);

        ResponseEntity actual = controller.createCustomer(newCustomer);

        assertThat(actual.getStatusCode()).isEqualTo(CREATED);
        assertThat(actual.getBody()).isEqualTo(String.format("Saved new customer %s.", uuid));
    }

    @Test
    public void givenANewCustomer_whenPostingNewCustomerFails_thenCustomerIsNotCreatedAndInternalServerErrorResponseIsReturned() throws FailedToSaveCustomerException, CustomerAlreadyExistsException {
        when(customerCommandService.createCustomer(newCustomer)).thenThrow(new FailedToSaveCustomerException("Failed to save new customer."));

        ResponseEntity actual = controller.createCustomer(newCustomer);

        assertThat(actual.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
        assertThat(actual.getBody()).isEqualTo("Failed to save new customer.");
    }

    @Test
    public void givenAnExistingCustomer_whenPostingExistingCustomer_thenCustomerIsNotCreatedAndInternalServerErrorResponseIsReturned() throws FailedToSaveCustomerException, CustomerAlreadyExistsException {
        CustomerDto existingCustomer = CustomerDto.builder().uuid(uuid).build();

        when(customerCommandService.createCustomer(existingCustomer)).thenThrow(new CustomerAlreadyExistsException(uuid));

        ResponseEntity actual = controller.createCustomer(existingCustomer);

        assertThat(actual.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
        assertThat(actual.getBody()).isEqualTo(String.format("Customer with UUID %s already exists.", uuid));
    }

    @Test
    public void givenAnExistingCustomer_whenPuttingUpdatedCustomer_thenCustomerIsUpdatedAndOkResponseIsReturned() throws Exception {
        CustomerDto existingCustomer = CustomerDto.builder().uuid(uuid).firstName("John").surname("Doe").build();

        when(customerCommandService.updateCustomer(existingCustomer)).thenReturn(existingCustomer);

        ResponseEntity actual = controller.updateCustomer(existingCustomer);

        assertThat(actual.getStatusCode()).isEqualTo(OK);
        assertThat(actual.getHeaders().get("Location")).contains(String.format("/customers/%s", uuid));
        assertThat(actual.getBody()).isEqualTo(String.format("Updated customer with UUID %s.", uuid));
    }

    @Test
    public void givenAnExistingCustomer_whenPuttingUpdatedCustomerFails_thenCustomerIsNotUpdatedAndInternalServerErrorResponseIsReturned() throws Exception {
        CustomerDto existingCustomer = CustomerDto.builder().uuid(uuid).firstName("John").surname("Doe").build();

        when(customerCommandService.updateCustomer(existingCustomer)).thenThrow(new FailedToUpdateCustomerException(existingCustomer));

        ResponseEntity actual = controller.updateCustomer(existingCustomer);

        assertThat(actual.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
        assertThat(actual.getHeaders()).isEmpty();
        assertThat(actual.getBody()).isEqualTo(String.format("Failed to update customer with UUID %s.", uuid));
    }

    @Test
    public void givenANewCustomer_whenPuttingNewCustomer_thenCustomerIsNotCreatedAndBadRequestResponseIsReturned() throws Exception {
        CustomerDto newCustomer = CustomerDto.builder().firstName("John").surname("Doe").build();

        when(customerCommandService.updateCustomer(newCustomer)).thenThrow(new CannotUpdateNewCustomerException());

        ResponseEntity actual = controller.updateCustomer(newCustomer);

        assertThat(actual.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(actual.getHeaders()).isEmpty();
        assertThat(actual.getBody()).isEqualTo(String.format("Cannot update a new customer.", uuid));
    }
}
