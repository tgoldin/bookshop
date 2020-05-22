package com.tpg.bookshop.web.controllers;

import com.tpg.bookshop.UUIDBasedTest;
import com.tpg.bookshop.services.BookCommandService;
import com.tpg.bookshop.services.exceptions.BookAlreadyExistsException;
import com.tpg.bookshop.services.exceptions.CannotUpdateNewBookException;
import com.tpg.bookshop.services.exceptions.FailedToSaveBookException;
import com.tpg.bookshop.web.model.BookDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
public class BookCommandControllerTests extends UUIDBasedTest {

    @Mock
    private BookCommandService bookCommandService;

    private BookDto newBook;

    @InjectMocks
    private BookCommandController controller;

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
    public void givenANewBook_whenPosted_thenBookIsCreatedAndCreatedResponseIsReturned() throws FailedToSaveBookException, BookAlreadyExistsException {
        BookDto savedBook = BookDto.builder()
                .uuid(uuid)
                .isbn("1234-ABC")
                .title("A new book")
                .description("A nice new book")
                .build();

        when(bookCommandService.createBook(newBook)).thenReturn(savedBook);

        ResponseEntity actual = controller.createBook(newBook);

        assertThat(actual.getStatusCode()).isEqualTo(CREATED);
        assertThat(actual.getHeaders().get("Location")).contains(String.format("/books/%s", uuid));
        assertThat(actual.getBody()).isEqualTo(String.format("Saved new book %s", uuid));

        verify(bookCommandService).createBook(newBook);
    }

    @Test
    public void givenANewBook_whenPostingFails_thenBookIsNotCreatedAndInternalServerErrorResponseIsReturned() throws FailedToSaveBookException, BookAlreadyExistsException {
        when(bookCommandService.createBook(newBook)).thenThrow(new FailedToSaveBookException("Failed to save new book"));

        ResponseEntity actual = controller.createBook(newBook);

        assertThat(actual.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
        assertThat(actual.getHeaders()).isEmpty();
        assertThat(actual.getBody()).isEqualTo("Failed to save new book");
    }

    @Test
    public void givenAnExistingBook_whenPosted_thenBookIsNotCreatedAndInternalServerErrorResponseIsReturned() throws FailedToSaveBookException, BookAlreadyExistsException {
        BookDto existingBook = BookDto.builder().uuid(uuid).build();

        when(bookCommandService.createBook(existingBook)).thenThrow(new BookAlreadyExistsException(existingBook.getUuid()));

        ResponseEntity actual = controller.createBook(existingBook);

        assertThat(actual.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
        assertThat(actual.getHeaders()).isEmpty();
        assertThat(actual.getBody()).isEqualTo(String.format("Book with UUID %s already exists.", uuid));
    }

    @Test
    public void givenAnExistingBook_whenPuttingUpdatedBook_thenBookIsUpdatedAndNoContentResponseIsReturned() throws CannotUpdateNewBookException {
        BookDto existingBook = BookDto.builder().uuid(uuid).title("Title One").build();

        when(bookCommandService.updateBook(existingBook)).thenReturn(existingBook);

        ResponseEntity actual = controller.updateBook(existingBook);

        assertThat(actual.getStatusCode()).isEqualTo(OK);
        assertThat(actual.getHeaders().get("Location")).contains(String.format("/books/%s", uuid));
        assertThat(actual.getBody()).isEqualTo(String.format("Updated book with UUID %s.", uuid));
    }

    @Test
    public void givenANewBook_whenPuttingNewBook_thenBookIsNotCreatedAndBadRequestResponseIsReturned() throws CannotUpdateNewBookException {
        when(bookCommandService.updateBook(newBook)).thenThrow(new CannotUpdateNewBookException());

        ResponseEntity actual = controller.updateBook(newBook);

        assertThat(actual.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(actual.getHeaders()).isEmpty();
        assertThat(actual.getBody()).isEqualTo("Cannot update a new book.");
    }
}
