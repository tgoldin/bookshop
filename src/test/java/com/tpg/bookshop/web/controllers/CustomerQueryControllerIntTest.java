package com.tpg.bookshop.web.controllers;

import org.junit.jupiter.api.Test;

import static com.tpg.bookshop.services.BookUuids.NOT_FOUND_UUID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerQueryControllerIntTest extends WebIntTest {

    @Test
    public void givenUuid_whenFindingCustomerByUuid_thenCustomerWithUuidReturned() throws Exception {

        mockMvc.perform(get("/customers/{uuid}", uuid)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").isNotEmpty());
    }

    @Test
    public void givenUuidAndNoCustomerWithMatchingUuid_whenFindingCustomerByUuid_thenEmptyBodyReturnedAndResponseIsNotFound() throws Exception {

        mockMvc.perform(get("/customers/{uuid}", NOT_FOUND_UUID)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }
}
