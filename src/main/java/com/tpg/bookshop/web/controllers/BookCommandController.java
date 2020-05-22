package com.tpg.bookshop.web.controllers;

import com.tpg.bookshop.services.BookCommandService;
import com.tpg.bookshop.services.exceptions.BookAlreadyExistsException;
import com.tpg.bookshop.services.exceptions.FailedToSaveBookException;
import com.tpg.bookshop.web.model.BookDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
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
}
