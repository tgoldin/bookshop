package com.tpg.bookshop.web.controller;

import com.tpg.bookshop.UUIDBasedTest;
import com.tpg.bookshop.services.CustomerQueryService;
import com.tpg.bookshop.web.model.CustomerDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerWebMvcTests extends UUIDBasedTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerQueryService customerQueryService;

    @Test
    public void givenUuid_whenFindingCustomerByUuid_thenCustomerWithUuidReturned() throws Exception {

        CustomerDto customerDto = CustomerDto.builder().uuid(uuid).build();

        when(customerQueryService.findByUuid(uuid)).thenReturn(of(customerDto));

        mockMvc.perform(get("/customers/{uuid}", uuid)
            .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.uuid").isNotEmpty());
    }

    @Test
    public void givenUuidAndNoCustomerMatchingUuid_whenFindingCustomerByUuid_thenResponseStatusIsNotFoundAndEmptyBodyReturned() throws Exception {

        when(customerQueryService.findByUuid(uuid)).thenReturn(empty());

        mockMvc.perform(get("/books/{uuid}", uuid)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }
}
