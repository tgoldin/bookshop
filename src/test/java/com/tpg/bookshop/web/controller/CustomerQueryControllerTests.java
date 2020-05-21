package com.tpg.bookshop.web.controller;

import com.tpg.bookshop.UUIDBasedTest;
import com.tpg.bookshop.services.CustomerQueryService;
import com.tpg.bookshop.web.model.CustomerDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class CustomerQueryControllerTests extends UUIDBasedTest {
    @Mock
    private CustomerQueryService customerQueryService;

    @InjectMocks
    private CustomerQueryController controller;

    @Test
    public void givenExistingCustomerUuid_whenFindingCustomerByUuid_thenCustomerIsReturned() {

        CustomerDto customerDto = CustomerDto.builder().uuid(uuid).build();

        when(customerQueryService.findByUuid(uuid)).thenReturn(of(customerDto));

        ResponseEntity<CustomerDto> actual = controller.findByUuid(uuid);

        assertThat(actual.getStatusCode()).isEqualTo(OK);
        assertThat(actual.getBody().getUuid()).isEqualTo(uuid);
    }

    @Test
    public void whenNoCustomerExistsWithUuid_thenResponseIsEmptyAndNotFound() {
        when(customerQueryService.findByUuid(uuid)).thenReturn(empty());

        ResponseEntity<CustomerDto> actual = controller.findByUuid(uuid);

        assertThat(actual.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(actual.getBody()).isEqualTo(null);
    }
}
