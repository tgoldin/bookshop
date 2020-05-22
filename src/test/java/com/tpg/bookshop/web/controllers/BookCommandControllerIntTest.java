package com.tpg.bookshop.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpg.bookshop.BookshopApplication;
import com.tpg.bookshop.UUIDBasedTest;
import com.tpg.bookshop.web.model.BookDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static com.tpg.bookshop.services.BookUuids.NOT_FOUND_UUID;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = BookshopApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(profiles={"int-test"})
public class BookCommandControllerIntTest extends UUIDBasedTest {
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private BookDto newBook;

    @BeforeEach
    public void setUp() {
        super.setUp();

        objectMapper = new ObjectMapper();

        newBook = BookDto.builder()
                .isbn("1234-ABC")
                .title("A new book")
                .description("A nice new book")
                .build();
    }

    @Test
    public void givenANewBook_whenPosted_thenBookIsCreatedAndCreatedResponseIsReturned() throws Exception {

        String json = objectMapper.writeValueAsString(newBook);

        mockMvc.perform(post("/books")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Saved new book")));
    }

    @Test
    public void givenANewBook_whenPostingFails_thenBookIsNotCreatedAndInternalServerErrorResponseIsReturned() throws Exception {
        newBook.setUuid(NOT_FOUND_UUID);

        String json = objectMapper.writeValueAsString(newBook);

        mockMvc.perform(post("/books")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Failed to save new book")));
    }

    @Test
    public void givenAnExistingBook_whenPosted_thenBookIsNotCreatedAndInternalServerErrorResponseIsReturned() throws Exception {

        BookDto existingBook = BookDto.builder().uuid(uuid).build();

        String json = objectMapper.writeValueAsString(existingBook);

        mockMvc.perform(post("/books")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString(String.format("Book with UUID %s already exists", uuid))));
    }
}
