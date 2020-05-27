package com.tpg.bookshop.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tpg.bookshop.web.model.CustomerDto;
import org.junit.jupiter.api.Test;

import static com.tpg.bookshop.services.BookUuids.NOT_FOUND_UUID;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomerCommandRATests extends WebRATests {
    private static final String CUSTOMERS_URI = "/customers";

    @Test
    public void givenNewCustomer_whenPostingNewCustomer_thenExpectCreatedResponseAndCustomerIsCreated() throws JsonProcessingException {
        CustomerDto newCustomer = CustomerDto.builder().firstName("John").surname("Doe").build();

        String json = objectMapper.writeValueAsString(newCustomer);

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .body(json)
            .log().all().
        when()
            .post(CUSTOMERS_URI).
        then()
            .log().body()
            .assertThat().statusCode(SC_CREATED)
            .body(containsString("Saved new customer"));
    }

    @Test
    public void givenNewCustomer_whenPostingNewCustomerFails_thenExpectInternalServerErrorResponseAndCustomerIsNotCreated() throws JsonProcessingException {
        CustomerDto newCustomer = CustomerDto.builder().uuid(NOT_FOUND_UUID).firstName("John").surname("Doe").build();

        String json = objectMapper.writeValueAsString(newCustomer);

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .body(json)
            .log().all().
        when()
            .post(CUSTOMERS_URI).
        then()
            .log().body()
            .assertThat().statusCode(SC_INTERNAL_SERVER_ERROR)
            .body(containsString("Failed to save new customer"));
    }

    @Test
    public void givenAnExistingCustomer_whenPostingExistingCustomer_thenExpectBadRequestResponseAndCustomerIsNotUpdated() throws JsonProcessingException {
        CustomerDto existingCustomer = CustomerDto.builder().uuid(uuid).firstName("John").surname("Doe").build();

        String json = objectMapper.writeValueAsString(existingCustomer);

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .body(json)
            .log().all().
        when()
            .post(CUSTOMERS_URI).
        then()
            .log().body()
            .assertThat().statusCode(SC_INTERNAL_SERVER_ERROR)
            .body(containsString(String.format("Customer with UUID %s already exists.", uuid)));
    }

    @Test
    public void givenAnExistingCustomer_whenPuttingExistingCustomer_thenExpectOkResponseAndCustomerIsUpdated() throws JsonProcessingException {
        CustomerDto newCustomer = CustomerDto.builder().uuid(uuid).firstName("John").surname("Doe").build();

        String json = objectMapper.writeValueAsString(newCustomer);

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .body(json)
            .log().all().
        when()
            .put(CUSTOMERS_URI).
        then()
            .log().body()
            .assertThat().statusCode(SC_OK)
                .body(containsString(String.format("Updated customer with UUID %s.", uuid)));
    }

    @Test
    public void givenAnExistingCustomer_whenPuttingExistingCustomerFails_thenExpectInternalServerErrorResponseAndCustomerIsNotUpdated() throws JsonProcessingException {
        CustomerDto newCustomer = CustomerDto.builder().uuid(uuid).firstName("John").surname(uuid.toString()).build();

        String json = objectMapper.writeValueAsString(newCustomer);

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .body(json)
            .log().all().
        when()
            .put(CUSTOMERS_URI).
        then()
            .log().body()
            .assertThat().statusCode(SC_INTERNAL_SERVER_ERROR)
                .body(containsString(String.format("Failed to update customer with UUID %s.", uuid)));
    }

    @Test
    public void givenANewCustomer_whenPuttingNewCustomer_thenExpectBadRequestResponseAndCustomerIsNotCreated() throws JsonProcessingException {
        CustomerDto newCustomer = CustomerDto.builder().firstName("John").surname("Doe").build();

        String json = objectMapper.writeValueAsString(newCustomer);

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .body(json)
            .log().all().
        when()
            .put(CUSTOMERS_URI).
        then()
            .log().body()
            .assertThat().statusCode(SC_BAD_REQUEST)
                .body(containsString("Cannot update a new customer."));
    }
}
