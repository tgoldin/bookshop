package com.tpg.bookshop.web.controller;

import com.tpg.bookshop.web.model.BookDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

public class BookControllerTests {
    private BookController controller;

    @BeforeEach
    public void setUp() {
        controller = new BookController();
    }

    @Test
    public void whenFindingBookByUuid_thenExistingBookWithMatchingUuidIsFound() {
        UUID uuid = UUID.randomUUID();

        ResponseEntity<BookDto> actual = controller.findByUuid(uuid);

        assertThat(actual.getStatusCode()).isEqualTo(OK);
        assertThat(actual.getBody().getUuid()).isEqualTo(uuid);
    }
}
