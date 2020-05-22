package com.tpg.bookshop.web.controllers;

import com.tpg.bookshop.services.BookCommandService;
import com.tpg.bookshop.services.exceptions.BookAlreadyExistsException;
import com.tpg.bookshop.services.exceptions.FailedToSaveBookException;
import com.tpg.bookshop.web.model.BookDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
public class BookCommandController {
    private static final String BOOKS_COMMAND_URI = "/books";
    private static final String HEADER_LOCATION_KEY = "Location";

    private final BookCommandService bookCommandService;

    public BookCommandController(BookCommandService bookCommandService) {
        this.bookCommandService = bookCommandService;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity createBook(@RequestBody BookDto bookDto) {
        try {
            BookDto savedBook = bookCommandService.createBook(bookDto);
            return new ResponseEntity(String.format("Saved new book %s", savedBook.getUuid()), generateHttpHeaders(savedBook), CREATED);
        }
        catch (FailedToSaveBookException | BookAlreadyExistsException fsbe) {
            return new ResponseEntity(fsbe.getMessage(), generateHttpHeaders(null), INTERNAL_SERVER_ERROR);
        }
    }

    private HttpHeaders generateHttpHeaders(BookDto bookDto) {
        HttpHeaders httpHeaders = new HttpHeaders();

        if (bookDto == null) { return httpHeaders; }

        httpHeaders.add(HEADER_LOCATION_KEY, String.format("%s/%s", BOOKS_COMMAND_URI, bookDto.getUuid()));

        return httpHeaders;
    }
}
