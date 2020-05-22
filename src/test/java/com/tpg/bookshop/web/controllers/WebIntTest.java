package com.tpg.bookshop.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpg.bookshop.BookshopApplication;
import com.tpg.bookshop.UUIDBasedTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = BookshopApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(profiles={"int-test"})
public abstract class WebIntTest extends UUIDBasedTest {
    @Autowired
    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        super.setUp();

        objectMapper = new ObjectMapper();
    }
}
