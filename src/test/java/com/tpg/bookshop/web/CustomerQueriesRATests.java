package com.tpg.bookshop.web;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static com.tpg.bookshop.services.BookUuids.NOT_FOUND_UUID;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ActiveProfiles({"qa"})
public class CustomerQueriesRATests extends WebRATests {

    public static final String CUSTOMERS_BY_UUID_URI = "/customers/{uuid}";

    @Test
    public void givenUuidOfExistingCustomer_whenFindByUuid_thenExpectValidResponse() {
        given()
            .contentType(APPLICATION_JSON_VALUE)
            .pathParam("uuid", uuid)
            .log().all().
        when().get(CUSTOMERS_BY_UUID_URI).
        then()
            .log().body()
            .assertThat().statusCode(SC_OK)
            .body("uuid", is(uuid.toString()));
    }

    @Test
    public void givenUuidAndNoCustomerWithMatchingUuid_whenFindByUuid_thenExpectNotFoundResponse() {
        java.util.UUID notFoundUuid = NOT_FOUND_UUID;

        given()
                .contentType(APPLICATION_JSON_VALUE)
                .pathParam("uuid", notFoundUuid)
                .log().all().
                when().get(CUSTOMERS_BY_UUID_URI).
                then()
                .log().body()
                .assertThat().statusCode(SC_NOT_FOUND)
                .body(is(""));
    }

    @Test
    public void givenMeasureResponseTime_thenResponseTimeIsMeasured() {
        given()
            .contentType(APPLICATION_JSON_VALUE)
            .pathParam("uuid", uuid)
            .log().all().
        when().get(CUSTOMERS_BY_UUID_URI).
        then()
            .log().body()
            .assertThat().statusCode(SC_OK)
            .time(lessThan(1500L));
    }
}
