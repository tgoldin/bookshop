package com.tpg.bookshop.web.controllers;

import com.tpg.bookshop.services.exceptions.CannotUpdateNewBookException;
import com.tpg.bookshop.web.model.BookDto;
import com.tpg.bookshop.web.model.NewBookRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.tpg.bookshop.services.BookUuids.NOT_FOUND_UUID;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookCommandControllerIntTest extends WebIntTest {
    private NewBookRequest newBookRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();

        newBookRequest = NewBookRequest.builder()
                .isbn("1234-ABC")
                .title("A new book")
                .description("A nice new book")
                .build();
    }

    @Test
    public void givenANewBook_whenPostingNewBook_thenBookIsCreatedAndCreatedResponseIsReturned() throws Exception {

        String json = objectMapper.writeValueAsString(newBookRequest);

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
    public void givenANewBook_whenPostingNewBookFails_thenBookIsNotCreatedAndInternalServerErrorResponseIsReturned() throws Exception {
        newBookRequest.setTitle(uuid.toString());

        String json = objectMapper.writeValueAsString(newBookRequest);

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
    public void givenAnExistingBook_whenPostingExistingBook_thenBookIsNotCreatedAndBadRequestResponseIsReturned() throws Exception {
        newBookRequest.setDescription(uuid.toString());

        String json = objectMapper.writeValueAsString(newBookRequest);

        mockMvc.perform(post("/books")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Location", is(nullValue())))
                .andExpect(content().string(containsString(String.format("Book with UUID %s already exists", uuid))));
    }

    @Test
    public void givenAnExistingBook_whenPuttingUpdatedBook_thenBookIsUpdatedAndOkResponseIsReturned() throws Exception {
        BookDto existingBook = BookDto.builder().uuid(uuid).title("Title One").build();

        String json = objectMapper.writeValueAsString(existingBook);

        mockMvc.perform(put("/books")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Location", String.format("/books/%s", uuid)))
                .andExpect(content().string(containsString(String.format("Updated book with UUID %s.", uuid))));
    }

    @Test
    public void givenAnExistingBook_whenPuttingUpdatedBookFails_thenBookIsNotUpdatedAndInternalErrorResponseIsReturned() throws Exception {
        BookDto existingBook = BookDto.builder().uuid(NOT_FOUND_UUID).title("Title One").description(NOT_FOUND_UUID.toString())
                .build();

        String json = objectMapper.writeValueAsString(existingBook);

        mockMvc.perform(put("/books")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(header().string("Location", is(nullValue())))
                .andExpect(content().string(containsString(String.format("Failed to update book with UUID %s.", NOT_FOUND_UUID))));
    }

    @Test
    public void givenANewBook_whenPuttingNewBook_thenBookIsNotCreatedAndBadRequestResponseIsReturned() throws Exception {
        newBookRequest.setTitle(uuid.toString());

        String json = objectMapper.writeValueAsString(newBookRequest);

        mockMvc.perform(put("/books")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Location", is(nullValue())))
                .andExpect(content().string(containsString("Cannot update a new book.")));
    }
}
