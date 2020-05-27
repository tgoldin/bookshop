package com.tpg.bookshop.web.controllers;

import com.tpg.bookshop.UUIDBasedTest;
import com.tpg.bookshop.services.BookCommandService;
import com.tpg.bookshop.services.exceptions.BookAlreadyExistsException;
import com.tpg.bookshop.services.exceptions.CannotUpdateNewBookException;
import com.tpg.bookshop.services.exceptions.FailedToSaveBookException;
import com.tpg.bookshop.services.exceptions.FailedToUpdateBookException;
import com.tpg.bookshop.web.model.BookDto;
import com.tpg.bookshop.web.model.NewBookRequest;
import com.tpg.bookshop.web.model.UpdateBookRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
public class BookCommandControllerTests extends UUIDBasedTest {

    @Mock
    private BookCommandService bookCommandService;

    private NewBookRequest newBookRequest;

    private UpdateBookRequest updateBookRequest;

    @InjectMocks
    private BookCommandController controller;

    @BeforeEach
    public void setUp() {
        super.setUp();

        newBookRequest = NewBookRequest.builder()
                .isbn("1234-ABC")
                .title("A new book")
                .description("A nice new book")
                .build();

        updateBookRequest = UpdateBookRequest.builder()
                .uuid(uuid)
                .isbn("1234-ABC")
                .title("A new book")
                .description("A nice new book")
                .build();
    }

    @Test
    public void givenANewBook_whenPostingNewBook_thenBookIsCreatedAndCreatedResponseIsReturned() throws Exception {
        BookDto savedBook = BookDto.builder()
                .uuid(uuid)
                .isbn("1234-ABC")
                .title("A new book")
                .description("A nice new book")
                .build();

        when(bookCommandService.createBook(newBookRequest)).thenReturn(savedBook);

        ResponseEntity actual = controller.createBook(newBookRequest);

        assertThat(actual.getStatusCode()).isEqualTo(CREATED);
        assertThat(actual.getHeaders().get("Location")).contains(String.format("/books/%s", uuid));
        assertThat(actual.getBody()).isEqualTo(String.format("Saved new book %s", uuid));

        verify(bookCommandService).createBook(newBookRequest);
    }

    @Test
    public void givenANewBook_whenPostingNewBookFails_thenBookIsNotCreatedAndInternalServerErrorResponseIsReturned() throws Exception {
        when(bookCommandService.createBook(newBookRequest)).thenThrow(new FailedToSaveBookException("Failed to save new book"));

        ResponseEntity actual = controller.createBook(newBookRequest);

        assertThat(actual.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
        assertThat(actual.getHeaders()).isEmpty();
        assertThat(actual.getBody()).isEqualTo("Failed to save new book");
    }

    @Test
    public void givenAnExistingBook_whenPosted_thenBookIsNotCreatedAndInternalServerErrorResponseIsReturned() throws Exception {
        NewBookRequest request = NewBookRequest.builder().title(uuid.toString()).build();

        when(bookCommandService.createBook(request)).thenThrow(new BookAlreadyExistsException(uuid));

        ResponseEntity actual = controller.createBook(request);

        assertThat(actual.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(actual.getHeaders()).isEmpty();
        assertThat(actual.getBody()).isEqualTo(String.format("Book with UUID %s already exists.", uuid));
    }

    @Test
    public void givenAnExistingBook_whenPuttingUpdatedBook_thenBookIsUpdatedAndOkResponseIsReturned() throws Exception {
        BookDto existingBook = BookDto.builder().uuid(uuid).title("Title One").build();

        when(bookCommandService.updateBook(updateBookRequest)).thenReturn(existingBook);

        ResponseEntity actual = controller.updateBook(updateBookRequest);

        assertThat(actual.getStatusCode()).isEqualTo(OK);
        assertThat(actual.getHeaders().get("Location")).contains(String.format("/books/%s", uuid));
        assertThat(actual.getBody()).isEqualTo(String.format("Updated book with UUID %s.", uuid));
    }

    @Test
    public void givenAnExistingBook_whenPuttingUpdatedBookFails_thenBookIsNotUpdatedAndInternalServerErrorResponseIsReturned() throws Exception {
        BookDto existingBook = BookDto.builder().uuid(uuid).title("Title One").build();

        when(bookCommandService.updateBook(updateBookRequest)).thenThrow(new FailedToUpdateBookException(uuid));

        ResponseEntity actual = controller.updateBook(updateBookRequest);

        assertThat(actual.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
        assertThat(actual.getHeaders().isEmpty());
        assertThat(actual.getBody()).isEqualTo(String.format("Failed to update book with UUID %s.", uuid));
    }

    @Test
    public void givenANewBook_whenPuttingNewBook_thenBookIsNotCreatedAndBadRequestResponseIsReturned() throws Exception {
        updateBookRequest.setUuid(null);

        when(bookCommandService.updateBook(updateBookRequest)).thenThrow(new CannotUpdateNewBookException());

        ResponseEntity actual = controller.updateBook(updateBookRequest);

        assertThat(actual.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(actual.getHeaders()).isEmpty();
        assertThat(actual.getBody()).isEqualTo("Cannot update a new book.");
    }
}
