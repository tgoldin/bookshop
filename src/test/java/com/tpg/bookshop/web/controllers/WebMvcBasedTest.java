package com.tpg.bookshop.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tpg.bookshop.UUIDBasedTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
public abstract class WebMvcBasedTest extends UUIDBasedTest {
    @Autowired
    protected MockMvc mockMvc;

    protected ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        super.setUp();

        objectMapper = new ObjectMapper();
    }
}
