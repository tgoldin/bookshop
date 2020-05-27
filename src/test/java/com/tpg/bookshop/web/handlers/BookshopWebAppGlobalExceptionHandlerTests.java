package com.tpg.bookshop.web.handlers;

import com.tpg.bookshop.web.controllers.BookCommandController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
public class BookshopWebAppGlobalExceptionHandlerTests {
    private MethodParameter methodParameter;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private WebRequest webRequest;

    private BookshopWebAppGlobalExceptionHandler handler;

    @BeforeEach
    public void setUp() {
        Method method = Stream.of(BookCommandController.class.getMethods()).filter(m -> m.getName().equals("createBook")).findFirst().get();

        methodParameter = new MethodParameter(method, 0);

        handler = new BookshopWebAppGlobalExceptionHandler();
    }

    @Test
    public void givenNewBookRequestMissingTitle_whenValidated_thenErrorsAreAvailableInTheResponseBody() {
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);

        FieldError fieldError = new FieldError("newBookRequest", "title", "title cannot be empty");

        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<Object> actual = handler.handleMethodArgumentNotValid(ex, new HttpHeaders(), BAD_REQUEST, webRequest);

        assertThat(actual.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(actual.getHeaders()).isEmpty();

        Map<String, Object> actualMap = (Map<String, Object>) actual.getBody();

        List<String> actualErrors = (List<String>) actualMap.get("errors");

        assertThat(actualErrors).hasSize(1);
        assertThat(actualErrors.get(0)).isEqualTo("title cannot be empty");

        assertThat(actualMap.get("timestamp")).isNotNull();
        assertThat(actualMap.get("status")).isEqualTo(BAD_REQUEST.value());
    }
}
