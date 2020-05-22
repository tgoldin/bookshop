package com.tpg.bookshop.web.controllers;

import com.tpg.bookshop.services.BookCommandService;
import com.tpg.bookshop.services.exceptions.BookAlreadyExistsException;
import com.tpg.bookshop.services.exceptions.CannotUpdateNewBookException;
import com.tpg.bookshop.services.exceptions.FailedToSaveBookException;
import com.tpg.bookshop.web.model.BookDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookCommandController.class)
public class BookCommandControllerWebMvcTests extends WebMvcBasedTest {
    @MockBean
    private BookCommandService bookCommandService;

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

        BookDto savedBook = BookDto.builder()
                .uuid(uuid)
                .isbn("1234-ABC")
                .title("A new book")
                .description("A nice new book")
                .build();

        when(bookCommandService.createBook(newBook)).thenReturn(savedBook);

        String json = objectMapper.writeValueAsString(newBook);

        mockMvc.perform(post("/books")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", is(equalTo(String.format("/books/%s", uuid)))))
                .andExpect(content().string(String.format("Saved new book %s", uuid)));
    }

    @Test
    public void givenANewBook_whenPostingFails_thenBookIsNotCreatedAndInternalServerErrorResponseIsReturned() throws Exception {

        when(bookCommandService.createBook(newBook)).thenThrow(new FailedToSaveBookException("Failed to save new book"));

        String json = objectMapper.writeValueAsString(newBook);

        mockMvc.perform(post("/books")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to save new book"));
    }

    @Test
    public void givenAnExistingBook_whenPosted_thenBookIsNotCreatedAndInternalServerErrorResponseIsReturned() throws Exception {

        BookDto existingBook = BookDto.builder().uuid(uuid).build();

        when(bookCommandService.createBook(existingBook)).thenThrow(new BookAlreadyExistsException(existingBook.getUuid()));

        String json = objectMapper.writeValueAsString(existingBook);

        mockMvc.perform(post("/books")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString(String.format("Book with UUID %s already exists.", uuid))));
    }

    @Test
    public void givenAnExistingBook_whenPuttingUpdatedBook_thenBookIsUpdatedAndOkResponseIsReturned() throws Exception {
        BookDto existingBook = BookDto.builder().uuid(uuid).title("Title One").build();

        when(bookCommandService.updateBook(existingBook)).thenReturn(existingBook);

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
    public void givenANewBook_whenPuttingNewBook_thenBookIsNotCreatedAndBadRequestResponseIsReturned() throws Exception {
        when(bookCommandService.updateBook(newBook)).thenThrow(new CannotUpdateNewBookException());

        String json = objectMapper.writeValueAsString(newBook);

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
