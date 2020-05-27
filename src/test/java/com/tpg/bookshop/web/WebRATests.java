package com.tpg.bookshop.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpg.bookshop.UUIDBasedTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"qa"})
public abstract class WebRATests extends UUIDBasedTest {
    protected ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        super.setUp();

        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;

        objectMapper = new ObjectMapper();
    }
}
