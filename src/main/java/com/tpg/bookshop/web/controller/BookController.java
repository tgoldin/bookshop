package com.tpg.bookshop.web.controller;

import com.tpg.bookshop.web.model.BookDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/books")
public class BookController {
    @GetMapping("/{uuid}")
    public ResponseEntity<BookDto> findByUuid(@PathVariable("uuid") UUID uuid) {
        return new ResponseEntity<>(BookDto.builder()
                .uuid(uuid).build(), OK);
    }
}
