package com.tpg.bookshop.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tpg.bookshop.web.model.BookDto;
import org.junit.jupiter.api.Test;

import static com.tpg.bookshop.services.BookUuids.EXISTING_UUID;
import static com.tpg.bookshop.services.BookUuids.NOT_FOUND_UUID;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class BookCommandsRATests extends WebRATests {
    private static final String BOOKS_URI = "/books";

    @Test
    public void givenNewBook_whenPostingNewBook_thenExpectOkResponseAndBookIsCreated() throws JsonProcessingException {
        BookDto newBook = BookDto.builder().title("An Title").build();

        String json = objectMapper.writeValueAsString(newBook);

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .body(json)
            .log().all().
        when()
            .post(BOOKS_URI).
        then()
            .log().body()
            .assertThat().statusCode(SC_CREATED)
            .body(containsString("Saved new book"));
    }

    @Test
    public void givenANewBook_whenPostingNewBookFails_thenExpectInternalServerErrorResponseAndBookIsNotCreated() throws JsonProcessingException {
        BookDto newBook = BookDto.builder().uuid(NOT_FOUND_UUID).title("An Title").description(NOT_FOUND_UUID.toString()).build();

        String json = objectMapper.writeValueAsString(newBook);

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .body(json)
            .log().all().
        when()
            .post(BOOKS_URI).
        then()
            .log().body()
            .assertThat().statusCode(SC_INTERNAL_SERVER_ERROR)
            .body(containsString("Failed to save new book"));
    }

    @Test
    public void givenANewBook_whenPuttingNewBook_thenExpectBookIsNotCreatedAndBadRequestResponse() throws JsonProcessingException {
        BookDto newBook = BookDto.builder().title("An Title").description(NOT_FOUND_UUID.toString()).build();

        String json = objectMapper.writeValueAsString(newBook);

        given()
                .contentType(APPLICATION_JSON_VALUE)
                .body(json)
                .log().all().
                when()
                .put(BOOKS_URI).
                then()
                .log().body()
                .assertThat().statusCode(SC_BAD_REQUEST)
                .body(containsString("Cannot update a new book"));
    }

    @Test
    public void givenAnExistingBook_whenPostingExistingBook_thenExpectBadRequestResponseAndBookIsNotCreated() throws JsonProcessingException {
        BookDto newBook = BookDto.builder().uuid(EXISTING_UUID).title("An Title").build();

        String json = objectMapper.writeValueAsString(newBook);

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .body(json)
            .log().all().
        when()
            .post(BOOKS_URI).
        then()
            .log().body()
            .assertThat().statusCode(SC_BAD_REQUEST)
            .body(containsString("Book with UUID"))
            .body(containsString("already exists."));
    }

    @Test
    public void givenAnExistingBook_whenPuttingExistingBook_thenExpectOkResponseAndBookIsUpdated() throws JsonProcessingException {
        BookDto newBook = BookDto.builder().uuid(EXISTING_UUID).title("An Title").build();

        String json = objectMapper.writeValueAsString(newBook);

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .body(json)
            .log().all().
        when()
            .put(BOOKS_URI).
        then()
            .log().body()
            .assertThat().statusCode(SC_OK)
            .body(containsString(String.format("Updated book with UUID %s", EXISTING_UUID)));
    }

    @Test
    public void givenAnExistingBook_whenPuttingExistingBookFails_thenExpectInternalServerErrorResponseAndBookIsNotUpdated() throws JsonProcessingException {
        BookDto newBook = BookDto.builder().uuid(NOT_FOUND_UUID).title("An Title").description(NOT_FOUND_UUID.toString()).build();

        String json = objectMapper.writeValueAsString(newBook);

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .body(json)
            .log().all().
        when()
            .put(BOOKS_URI).
        then()
            .log().body()
            .assertThat().statusCode(SC_INTERNAL_SERVER_ERROR)
            .body(containsString(String.format("Failed to update book with UUID %s", NOT_FOUND_UUID)));
    }
}
