package com.tpg.bookshop.web.controllers;

import com.tpg.bookshop.services.BookCommandService;
import com.tpg.bookshop.web.model.NewBookRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.isArray;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookCommandController.class)
public class BookCommandControllerValidationTests extends WebMvcBasedTest {
    private NewBookRequest newBookRequest;

    @MockBean
    private BookCommandService bookCommandService;

    @BeforeEach
    public void setUp() {
        super.setUp();

        newBookRequest = NewBookRequest.builder().title("a title").description("a description").isbn("1234-ABC").build();
    }

    @Test
    public void givenMissingTitle_whenPostingNewBook_thenBookIsNotCreatedAndBadRequestResponseIsReturned() throws Exception {
        newBookRequest.setTitle(null);

        String json = objectMapper.writeValueAsString(newBookRequest);

        mockMvc.perform(post("/books")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Location", is(nullValue())))
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(SC_BAD_REQUEST)))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("title must not be empty")));

        verify(bookCommandService, never()).createBook(newBookRequest);
    }

    @Test
    public void givenMissingDescription_whenPostingNewBook_thenBookIsNotCreatedAndBadRequestResponseIsReturned() throws Exception {
        newBookRequest.setDescription(null);

        String json = objectMapper.writeValueAsString(newBookRequest);

        mockMvc.perform(post("/books")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Location", is(nullValue())))
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(SC_BAD_REQUEST)))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("description must not be empty")));

        verify(bookCommandService, never()).createBook(newBookRequest);
    }

    @Test
    public void givenMissingISBN_whenPostingNewBook_thenBookIsNotCreatedAndBadRequestResponseIsReturned() throws Exception {
        newBookRequest.setIsbn(null);

        String json = objectMapper.writeValueAsString(newBookRequest);

        mockMvc.perform(post("/books")
                .contentType(APPLICATION_JSON)
                .content(json)
                .accept(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string("Location", is(nullValue())))
                .andExpect(jsonPath("$.timestamp", is(notNullValue())))
                .andExpect(jsonPath("$.status", is(SC_BAD_REQUEST)))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors", hasItem("ISBN must not be empty")));

        verify(bookCommandService, never()).createBook(newBookRequest);
    }
}
