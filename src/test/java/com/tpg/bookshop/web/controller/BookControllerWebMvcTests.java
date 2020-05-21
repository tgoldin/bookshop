package com.tpg.bookshop.web.controller;

import com.tpg.bookshop.UUIDBasedTest;
import com.tpg.bookshop.UUIDBuilder;
import com.tpg.bookshop.services.BookQueryService;
import com.tpg.bookshop.web.model.BookDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
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
@WebMvcTest(BookController.class)
public class BookControllerWebMvcTests extends UUIDBasedTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookQueryService bookQueryService;

    @Test
    public void givenUuid_whenFindingBookByUuid_thenBookWithUuidReturned() throws Exception {

        BookDto bookDto = BookDto.builder().uuid(uuid).build();

        when(bookQueryService.findByUuid(uuid)).thenReturn(of(bookDto));

        mockMvc.perform(get("/books/{uuid}", uuid)
            .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.uuid").isNotEmpty());
    }

    @Test
    public void givenUuidAndNoBookMatchingUuid_whenFindingBookByUuid_thenResponseStatusIsNotFoundAndEmptyBodyReturned() throws Exception {

        when(bookQueryService.findByUuid(uuid)).thenReturn(empty());

        mockMvc.perform(get("/books/{uuid}", uuid)
            .contentType(APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$").doesNotExist());
    }
}
