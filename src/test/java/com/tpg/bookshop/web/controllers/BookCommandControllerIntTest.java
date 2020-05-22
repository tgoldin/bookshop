package com.tpg.bookshop.web.controllers;

import com.tpg.bookshop.web.model.BookDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.tpg.bookshop.services.BookUuids.NOT_FOUND_UUID;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookCommandControllerIntTest extends WebIntTest {
    private BookDto newBook;

    @BeforeEach
    public void setUp() {
        super.setUp();

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
                .andExpect(header().string("Location", is(notNullValue())))
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
                .andExpect(header().string("Location", is(nullValue())))
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
                .andExpect(header().string("Location", is(nullValue())))
                .andExpect(content().string(containsString(String.format("Book with UUID %s already exists", uuid))));
    }
}
