package com.tpg.bookshop.web.controller;

import com.tpg.bookshop.BookshopApplication;
import com.tpg.bookshop.UUIDBasedTest;
import com.tpg.bookshop.UUIDBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = BookshopApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(profiles={"int-test"})
public class BookControllerIntTest extends UUIDBasedTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void givenUuid_whenFindingBookByUuid_thenBookWithUuidReturned() throws Exception {

        mockMvc.perform(get("/books/{uuid}", uuid)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uuid").isNotEmpty());
    }

    @Test
    public void givenUuidAndNoBookWithMatchingUuid_whenFindingBookByUuid_thenEmptyBodyReturnedAndResponseIsNotFound() throws Exception {

        java.util.UUID notFoundUuid = uuid(NOT_FOUND_UUID);

        mockMvc.perform(get("/books/{uuid}", notFoundUuid)
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").doesNotExist());
    }
}
