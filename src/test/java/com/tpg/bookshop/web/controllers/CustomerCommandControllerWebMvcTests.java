package com.tpg.bookshop.web.controllers;

import com.tpg.bookshop.services.CustomerCommandService;
import com.tpg.bookshop.services.exceptions.CannotUpdateNewCustomerException;
import com.tpg.bookshop.services.exceptions.CustomerAlreadyExistsException;
import com.tpg.bookshop.services.exceptions.FailedToSaveCustomerException;
import com.tpg.bookshop.services.exceptions.FailedToUpdateCustomerException;
import com.tpg.bookshop.web.model.CustomerDto;
import com.tpg.bookshop.web.model.NewCustomerRequest;
import com.tpg.bookshop.web.model.UpdateCustomerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerCommandController.class)
public class CustomerCommandControllerWebMvcTests extends WebMvcBasedTest {
    @MockBean
    private CustomerCommandService customerCommandService;

    private NewCustomerRequest newCustomerRequest;

    private UpdateCustomerRequest updateCustomerRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();

        newCustomerRequest = NewCustomerRequest.builder().firstName("john").surname("Doe").build();

        updateCustomerRequest = UpdateCustomerRequest.builder().uuid(uuid).firstName("John")
                .surname("doe").build();
    }

    @Test
    public void givenANewCustomer_whenPostingNewCustomer_thenCustomerIsCreatedAndCreateResponseIsReturned() throws Exception {
        CustomerDto savedCustomer = CustomerDto.builder().uuid(uuid).firstName("John").surname("Doe").build();

        when(customerCommandService.createCustomer(newCustomerRequest)).thenReturn(savedCustomer);

        String json = objectMapper.writeValueAsString(newCustomerRequest);

        mockMvc.perform(post("/customers")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", String.format("/customers/%s", uuid)))
                .andExpect(content().string(String.format("Saved new customer %s.", uuid)));
    }

    @Test
    public void givenANewCustomer_whenPostingFails_thenCustomerIsNotCreatedAndInternalServerErrorResponseIsReturned() throws Exception {
        when(customerCommandService.createCustomer(newCustomerRequest)).thenThrow(new FailedToSaveCustomerException("Failed to save new customer."));

        String json = objectMapper.writeValueAsString(newCustomerRequest);

        mockMvc.perform(post("/customers")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Location", is(nullValue())))
                .andExpect(content().string("Failed to save new customer."));
    }

    @Test
    public void givenAnExistingCustomer_whenPostingExistingCustomer_thenCustomerIsNotCreatedAndInternalServerErrorResponseIsReturned() throws Exception {
        when(customerCommandService.createCustomer(newCustomerRequest)).thenThrow(new CustomerAlreadyExistsException(uuid));

        String json = objectMapper.writeValueAsString(newCustomerRequest);

        mockMvc.perform(post("/customers")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Location", is(nullValue())))
                .andExpect(content().string(String.format("Customer with UUID %s already exists.", uuid)));
    }

    @Test
    public void givenAnExistingCustomer_whenPuttingUpdatedCustomer_thenCustomerIsUpdatedAndOkResponseIsReturned() throws Exception {
        CustomerDto existingCustomer = CustomerDto.builder().uuid(uuid).firstName("John").surname("Doe").build();

        when(customerCommandService.updateCustomer(updateCustomerRequest)).thenReturn(existingCustomer);

        String json = objectMapper.writeValueAsString(updateCustomerRequest);

        mockMvc.perform(put("/customers")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Location", is(String.format("/customers/%s", uuid))))
                .andExpect(content().string(String.format("Updated customer with UUID %s.", uuid)));
    }

    @Test
    public void givenAnExistingCustomer_whenPuttingUpdatedCustomerFails_thenCustomerIsNotUpdatedAndInternalServerErrorResponseIsReturned() throws Exception {
        updateCustomerRequest.setSurname(uuid.toString());

        when(customerCommandService.updateCustomer(updateCustomerRequest)).thenThrow(new FailedToUpdateCustomerException(uuid));

        String json = objectMapper.writeValueAsString(updateCustomerRequest);

        mockMvc.perform(put("/customers")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Location", is(nullValue())))
                .andExpect(content().string(String.format("Failed to update customer with UUID %s.", uuid)));
    }

    @Test
    public void givenANewCustomer_whenPuttingNewCustomer_thenCustomerIsNotCreatedAndBadRequestResponseIsReturned() throws Exception {
        when(customerCommandService.updateCustomer(updateCustomerRequest)).thenThrow(new CannotUpdateNewCustomerException());
        
        String json = objectMapper.writeValueAsString(updateCustomerRequest);

        mockMvc.perform(put("/customers")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Location", is(nullValue())))
                .andExpect(content().string("Cannot update a new customer."));
    }
}
