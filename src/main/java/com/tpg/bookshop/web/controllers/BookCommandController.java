package com.tpg.bookshop.web.controllers;

import com.tpg.bookshop.services.BookCommandService;
import com.tpg.bookshop.services.exceptions.BookAlreadyExistsException;
import com.tpg.bookshop.services.exceptions.CannotUpdateNewBookException;
import com.tpg.bookshop.services.exceptions.FailedToSaveBookException;
import com.tpg.bookshop.web.model.BookDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/books")
public class BookCommandController implements HttpHeadersBuilder {
    private static final String BOOKS_COMMAND_URI = "/books";

    private final BookCommandService bookCommandService;

    public BookCommandController(BookCommandService bookCommandService) {
        this.bookCommandService = bookCommandService;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createBook(@RequestBody BookDto bookDto) {
        try {
            BookDto savedBook = bookCommandService.createBook(bookDto);
            return new ResponseEntity(String.format("Saved new book %s", savedBook.getUuid()),
                    generateHttpHeaders(BOOKS_COMMAND_URI, savedBook.getUuid()), CREATED);
        }
        catch (FailedToSaveBookException | BookAlreadyExistsException fsbe) {
            return new ResponseEntity(fsbe.getMessage(), generateHttpHeaders(BOOKS_COMMAND_URI, null), INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity updateBook(@RequestBody BookDto bookDto) {
        try {
            BookDto updatedBook = bookCommandService.updateBook(bookDto);
            return new ResponseEntity(String.format("Updated book with UUID %s.", updatedBook.getUuid()), generateHttpHeaders(BOOKS_COMMAND_URI, updatedBook.getUuid()), OK);
        }
        catch (CannotUpdateNewBookException be) {
            return new ResponseEntity(be.getMessage(), generateHttpHeaders(BOOKS_COMMAND_URI, null), BAD_REQUEST);
        }
    }
}
