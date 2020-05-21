package com.tpg.bookshop.web.controller;

import com.tpg.bookshop.services.BookQueryService;
import com.tpg.bookshop.web.model.BookDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookQueryService bookQueryService;

    public BookController(BookQueryService bookQueryService) {
        this.bookQueryService = bookQueryService;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<BookDto> findByUuid(@PathVariable("uuid") UUID uuid) {
        return bookQueryService.findByUuid(uuid).map(book -> new ResponseEntity<>(book, OK))
                .orElse(new ResponseEntity<>(null, NOT_FOUND));
    }
}
