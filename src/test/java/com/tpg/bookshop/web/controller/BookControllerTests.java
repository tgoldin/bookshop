package com.tpg.bookshop.web.controller;

import com.tpg.bookshop.services.BookQueryService;
import com.tpg.bookshop.web.model.BookDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class BookControllerTests {
    @Mock
    private BookQueryService bookQueryService;

    @InjectMocks
    private BookController controller;

    @Test
    public void whenFindingBookByUuid_thenExistingBookWithMatchingUuidIsFound() {
        UUID uuid = UUID.randomUUID();

        BookDto bookDto = BookDto.builder().uuid(uuid).build();

        when(bookQueryService.findByUuid(uuid)).thenReturn(Optional.of(bookDto));

        ResponseEntity<BookDto> actual = controller.findByUuid(uuid);

        assertThat(actual.getStatusCode()).isEqualTo(OK);
        assertThat(actual.getBody().getUuid()).isEqualTo(uuid);
    }
}
