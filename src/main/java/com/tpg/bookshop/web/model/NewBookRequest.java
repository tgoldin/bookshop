package com.tpg.bookshop.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewBookRequest {
    @NotEmpty(message="title must not be empty")
    private String title;

    @NotEmpty(message="description must not be empty")
    private String description;

    @NotEmpty(message = "ISBN must not be empty")
    private String isbn;
}
