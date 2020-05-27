package com.tpg.bookshop.web.handlers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
public class BookshopWebAppGlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String TIMESTAMP_KEY = "timestamp";
    private static final String STATUS_KEY = "status";
    private static final String ERRORS_KEY = "errors";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders httpHeaders,
                                                                  HttpStatus status,
                                                                  WebRequest webRequest) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put(TIMESTAMP_KEY, new Date());
        body.put(STATUS_KEY, status.value());

        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(x -> x.getDefaultMessage())
                .collect(toList());

        body.put(ERRORS_KEY, errors);

        return new ResponseEntity<>(body, httpHeaders, status);
    }
}
