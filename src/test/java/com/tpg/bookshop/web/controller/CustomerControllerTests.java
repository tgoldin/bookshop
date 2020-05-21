package com.tpg.bookshop.web.controller;

import com.tpg.bookshop.web.model.CustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

public class CustomerControllerTests {
    private CustomerController controller;

    @BeforeEach
    public void setUp() {
        controller = new CustomerController();
    }

    @Test
    public void givenExistingCustomerUuid_whenFindingCustomerByUuid_thenCustomerIsReturned() {
        UUID uuid = UUID.randomUUID();

        ResponseEntity<CustomerDto> actual = controller.findByUuid(uuid);

        assertThat(actual.getStatusCode()).isEqualTo(OK);
        assertThat(actual.getBody().getUuid()).isEqualTo(uuid);
    }
}
