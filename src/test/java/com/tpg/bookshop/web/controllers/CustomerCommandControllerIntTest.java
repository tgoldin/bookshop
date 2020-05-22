package com.tpg.bookshop.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpg.bookshop.BookshopApplication;
import com.tpg.bookshop.UUIDBasedTest;
import com.tpg.bookshop.services.CustomerCommandService;
import com.tpg.bookshop.services.exceptions.CustomerAlreadyExistsException;
import com.tpg.bookshop.services.exceptions.FailedToSaveCustomerException;
import com.tpg.bookshop.web.model.CustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.tpg.bookshop.services.BookUuids.NOT_FOUND_UUID;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = BookshopApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(profiles={"int-test"})
public class CustomerCommandControllerIntTest extends UUIDBasedTest {
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private CustomerDto newCustomer;

    @BeforeEach
    public void setUp() {
        super.setUp();

        objectMapper = new ObjectMapper();

        newCustomer = CustomerDto.builder().firstName("john").surname("Doe").build();
    }

    @Test
    public void givenANewCustomer_whenPosted_thenCustomerIsCreatedAndCreateResponseIsReturned() throws Exception {
        String json = objectMapper.writeValueAsString(newCustomer);

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
        newCustomer.setUuid(NOT_FOUND_UUID);

        String json = objectMapper.writeValueAsString(newCustomer);

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
    public void givenAnExistingCustomer_whenPosted_thenCustomerIsNotCreatedAndInternalServerErrorResponseIsReturned() throws Exception {
        CustomerDto existingCustomer = CustomerDto.builder().uuid(uuid).firstName("John").surname("Doe").build();

        String json = objectMapper.writeValueAsString(existingCustomer);

        mockMvc.perform(post("/customers")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Location", is(nullValue())))
                .andExpect(content().string(String.format("Customer with UUID %s already exists.", uuid)));
    }
}
