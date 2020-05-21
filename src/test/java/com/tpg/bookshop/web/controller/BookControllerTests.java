package com.tpg.bookshop.web.controller;

import com.tpg.bookshop.UUIDBasedTest;
import com.tpg.bookshop.services.BookQueryService;
import com.tpg.bookshop.web.model.BookDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class BookControllerTests extends UUIDBasedTest {
    @Mock
    private BookQueryService bookQueryService;

    @InjectMocks
    private BookController controller;

    @Test
    public void whenFindingExistingBookByUuid_thenExistingBookWithMatchingUuidIsFound() {
        BookDto bookDto = BookDto.builder().uuid(uuid).build();

        when(bookQueryService.findByUuid(uuid)).thenReturn(Optional.of(bookDto));

        ResponseEntity<BookDto> actual = controller.findByUuid(uuid);

        assertThat(actual.getStatusCode()).isEqualTo(OK);
        assertThat(actual.getBody().getUuid()).isEqualTo(uuid);
    }

    @Test
    public void whenNoBookExistsWithUuid_thenResponseIsEmptyAndNotFound() {
        when(bookQueryService.findByUuid(uuid)).thenReturn(empty());

        ResponseEntity<BookDto> actual = controller.findByUuid(uuid);

        assertThat(actual.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(actual.getBody()).isEqualTo(null);
    }
}
