package com.tpg.bookshop.web.controllers;

import com.tpg.bookshop.web.model.CustomerDto;
import com.tpg.bookshop.web.model.NewCustomerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.tpg.bookshop.services.BookUuids.NOT_FOUND_UUID;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CustomerCommandControllerIntTest extends WebIntTest {

    private NewCustomerRequest newCustomerRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();

        newCustomerRequest = NewCustomerRequest.builder().firstName("john").surname("Doe").build();
    }

    @Test
    public void givenANewCustomer_whenPosted_thenCustomerIsCreatedAndCreateResponseIsReturned() throws Exception {
        String json = objectMapper.writeValueAsString(newCustomerRequest);

        mockMvc.perform(post("/customers")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is(notNullValue())))
                .andExpect(content().string(containsString("Saved new customer")));
    }

    @Test
    public void givenANewCustomer_whenPostingFails_thenCustomerIsNotCreatedAndInternalServerErrorResponseIsReturned() throws Exception {
        newCustomerRequest.setFirstName(NOT_FOUND_UUID.toString());

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
    public void givenAnExistingCustomer_whenPostingCustomer_thenCustomerIsNotCreatedAndInternalServerErrorResponseIsReturned() throws Exception {
        newCustomerRequest.setSurname(NOT_FOUND_UUID.toString());

        String json = objectMapper.writeValueAsString(newCustomerRequest);

        mockMvc.perform(post("/customers")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Location", is(nullValue())))
                .andExpect(content().string(String.format("Customer with UUID %s already exists.", NOT_FOUND_UUID)));
    }

    @Test
    public void givenAnExistingCustomer_whenPuttingCustomer_thenCustomerIsUpdatedAndOkResponseIsReturned() throws Exception {
        CustomerDto existingCustomer = CustomerDto.builder().uuid(uuid).firstName("John").surname("Doe").build();

        String json = objectMapper.writeValueAsString(existingCustomer);

        mockMvc.perform(put("/customers")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Location", is(equalTo(String.format("/customers/%s", uuid)))))
                .andExpect(content().string(String.format("Updated customer with UUID %s.", uuid)));
    }

    @Test
    public void givenAnExistingCustomer_whenPuttingUpdatedCustomerFails_thenCustomerIsNotUpdatedAndInternalServerErrorResponseIsReturned() throws Exception {
        CustomerDto existingCustomer = CustomerDto.builder().uuid(uuid).firstName("John").surname(uuid.toString()).build();

        String json = objectMapper.writeValueAsString(existingCustomer);

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
    public void givenANewCustomer_whenPuttingCustomer_thenCustomerIsNotCreatedAndBadRequestResponseIsReturned() throws Exception {
        CustomerDto existingCustomer = CustomerDto.builder().firstName("John").surname("Doe").build();

        String json = objectMapper.writeValueAsString(existingCustomer);

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
