package com.tpg.bookshop.web;

import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class BookQueriesRATests {
    private static final String UUID = "7debcc77-4ddf-41ae-8af6-999bb45014a2";
    public static final String BOOKS_BY_UUID_URI = "/books/{uuid}";

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    public void givenUuidOfExistingBook_whenFindByUuid_thenExpectValidResponse() {
        given()
            .contentType(APPLICATION_JSON_VALUE)
            .pathParam("uuid", UUID)
            .log().all().
        when().get(BOOKS_BY_UUID_URI).
        then()
            .log().body()
            .assertThat().statusCode(SC_OK)
            .body("uuid", is(UUID));
    }

    @Test
    public void givenMeasureResponseTime_thenResponseTimeIsMeasured() {
        given()
            .contentType(APPLICATION_JSON_VALUE)
            .pathParam("uuid", UUID)
            .log().all().
        when().get(BOOKS_BY_UUID_URI).
        then()
            .log().body()
            .assertThat().statusCode(SC_OK)
            .time(lessThan(1500L));
    }
}
