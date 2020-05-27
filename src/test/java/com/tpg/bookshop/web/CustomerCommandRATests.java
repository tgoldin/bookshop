package com.tpg.bookshop.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tpg.bookshop.web.model.CustomerDto;
import com.tpg.bookshop.web.model.NewCustomerRequest;
import com.tpg.bookshop.web.model.UpdateCustomerRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.tpg.bookshop.services.BookUuids.NOT_FOUND_UUID;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomerCommandRATests extends WebRATests {
    private static final String CUSTOMERS_URI = "/customers";

    private NewCustomerRequest newCustomerRequest;

    private UpdateCustomerRequest updateCustomerRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();

        newCustomerRequest = NewCustomerRequest.builder().firstName("John").surname("Doe").build();

        updateCustomerRequest = UpdateCustomerRequest.builder().uuid(uuid).firstName("John").surname("Doe").build();
    }

    @Test
    public void givenNewCustomer_whenPostingNewCustomer_thenExpectCreatedResponseAndCustomerIsCreated() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(newCustomerRequest);

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
        newCustomerRequest.setFirstName(NOT_FOUND_UUID.toString());

        String json = objectMapper.writeValueAsString(newCustomerRequest);

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
        newCustomerRequest.setSurname(NOT_FOUND_UUID.toString());

        String json = objectMapper.writeValueAsString(newCustomerRequest);

        given()
            .contentType(APPLICATION_JSON_VALUE)
            .body(json)
            .log().all().
        when()
            .post(CUSTOMERS_URI).
        then()
            .log().body()
            .assertThat().statusCode(SC_INTERNAL_SERVER_ERROR)
            .body(containsString(String.format("Customer with UUID %s already exists.", NOT_FOUND_UUID)));
    }

    @Test
    public void givenAnExistingCustomer_whenPuttingExistingCustomer_thenExpectOkResponseAndCustomerIsUpdated() throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(updateCustomerRequest);

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
        updateCustomerRequest.setSurname(uuid.toString());

        String json = objectMapper.writeValueAsString(updateCustomerRequest);

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
        String json = objectMapper.writeValueAsString(newCustomerRequest);

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
